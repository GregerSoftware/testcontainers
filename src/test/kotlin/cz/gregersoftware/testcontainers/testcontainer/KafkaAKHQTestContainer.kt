package cz.gregersoftware.testcontainers.testcontainer

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

@Import(KafkaAKHQTestContainerConfiguration::class)
annotation class KafkaAKHQTestContainer

@TestConfiguration(proxyBeanMethods = false)
private class KafkaAKHQTestContainerConfiguration {

    @Bean
    fun kafkaAKHQContainer(
        environment: Environment,
        kafkaContainer: KafkaContainer
    ): GenericContainer<*> =
        GenericContainer(DockerImageName.parse("tchiotludo/akhq:latest"))
            .applyGeneralConfiguration("kafka-akhq", environment)
            .withExposedPorts(8080)
            .withEnv(
                "AKHQ_CONFIGURATION",
                """
                akhq:
                    connections:
                        tescontainers-cluster:
                            properties:
                                bootstrap.servers: "PLAINTEXT://$KAFKA_LISTENER"
                """.trimIndent()
            )
            .dependsOn(kafkaContainer)
            .withNetwork(kafkaContainer.network)
}
package cz.gregersoftware.testcontainers.testcontainer

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.Network
import org.testcontainers.utility.DockerImageName

@Import(KafkaTestContainerConfiguration::class)
annotation class KafkaTestContainer

const val KAFKA_LISTENER = "kafka:19092"

@TestConfiguration(proxyBeanMethods = false)
private class KafkaTestContainerConfiguration {

    @ServiceConnection
    @Bean
    fun kafkaContainer(environment: Environment): KafkaContainer =
        KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
            .applyGeneralConfiguration("kafka", environment)
            .withNetwork(Network.newNetwork())
            .withListener { KAFKA_LISTENER }
}

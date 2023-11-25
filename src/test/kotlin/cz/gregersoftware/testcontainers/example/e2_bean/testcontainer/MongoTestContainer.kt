package cz.gregersoftware.testcontainers.example.e2_bean.testcontainer

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import java.util.*

@Import(MongoTestContainerConfiguration::class)
annotation class MongoTestContainer

@TestConfiguration(proxyBeanMethods = false)
private class MongoTestContainerConfiguration {

    @Bean
    fun mongoDBContainer(environment: Environment, registry: DynamicPropertyRegistry): MongoDBContainer {
        val mongoContainer = MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withCreateContainerCmdModifier { cmd ->
                cmd.withName("BEAN-${getApplicationName(environment)}-mongo-${UUID.randomUUID()}")
            }

        registry.add("spring.data.mongodb.uri", mongoContainer::getConnectionString)

        return mongoContainer
    }
}

private fun getApplicationName(environment: Environment) =
    environment.getRequiredProperty("spring.application.name")

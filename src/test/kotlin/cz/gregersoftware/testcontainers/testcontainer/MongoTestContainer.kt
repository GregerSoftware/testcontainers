package cz.gregersoftware.testcontainers.testcontainer

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

@Import(MongoTestContainerConfiguration::class)
annotation class MongoTestContainer

@TestConfiguration(proxyBeanMethods = false)
private class MongoTestContainerConfiguration {

    @ServiceConnection
    @Bean
    fun mongoDBContainer(environment: Environment): MongoDBContainer =
        MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .applyGeneralConfiguration("mongo", environment)
}

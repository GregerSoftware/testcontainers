package cz.gregersoftware.testcontainersexample.e3_serviceconnection.testcontainer

import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoConnectionDetails
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import java.util.*

/**
 * See
 *  * [ServiceConnection]
 *  * [ConnectionDetails]
 *  * [MongoConnectionDetails]
 *  * [MongoAutoConfiguration.MongoClientSettingsConfiguration]
 */
@Import(MongoTestContainerConfiguration::class)
annotation class MongoTestContainer

@TestConfiguration(proxyBeanMethods = false)
private class MongoTestContainerConfiguration {

    @ServiceConnection
    @Bean
    fun mongoDBContainer(environment: Environment): MongoDBContainer =
         MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withCreateContainerCmdModifier { cmd ->
                cmd.withName("SERVICE_CONNECTION-${getApplicationName(environment)}-mongo-${UUID.randomUUID()}")
            }
}

private fun getApplicationName(environment: Environment) =
    environment.getRequiredProperty("spring.application.name")

package cz.gregersoftware.testcontainersexample.e4_reuse.testcontainer

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.TestcontainersConfiguration
import java.util.*

@Import(MongoTestContainerConfiguration::class)
annotation class MongoTestContainer

@TestConfiguration(proxyBeanMethods = false)
private class MongoTestContainerConfiguration {

    @ServiceConnection
    @Bean
    fun mongoDBContainer(): MongoDBContainer =
         MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withCreateContainerCmdModifier { cmd ->
                cmd.withName("REUSE-mongo-${UUID.randomUUID()}")
            }
             .withReuse(TestcontainersConfiguration.getInstance().environmentSupportsReuse())
}

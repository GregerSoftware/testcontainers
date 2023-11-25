package cz.gregersoftware.testcontainers.example.e1_junit5.testcontainer

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import java.util.*

object MongoTestContainer {

    @Container
    val mongoContainer: MongoDBContainer =
        MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withCreateContainerCmdModifier { cmd ->
                cmd.withName("JUNIT-testcontainers-mongo-${UUID.randomUUID()}")
            }

    @JvmStatic
    @DynamicPropertySource
    fun registerProperties(registry: DynamicPropertyRegistry) {
        registry.add("spring.data.mongodb.uri", mongoContainer::getConnectionString)
    }
}

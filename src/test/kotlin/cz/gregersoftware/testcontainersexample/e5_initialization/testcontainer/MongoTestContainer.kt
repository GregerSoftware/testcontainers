package cz.gregersoftware.testcontainersexample.e5_initialization.testcontainer

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.TestcontainersConfiguration
import java.util.*

@Import(MongoTestContainerConfiguration::class)
annotation class MongoTestContainer

@TestConfiguration(proxyBeanMethods = false)
private class MongoTestContainerConfiguration {

//    abstract class InitializedContainer {
//        fun initialize() {}
//        fun reset() {}
//    }
//    class MongoDBInitializedContainer(container: MongoDBContainer):
//        InitializedContainer(),
//        MongoDBContainer by container

    @ServiceConnection
    @Bean
    fun mongoDBContainer(environment: Environment): MongoDBContainer =
        object : MongoDBContainer(DockerImageName.parse("mongo:latest")) {
            init {
                withCreateContainerCmdModifier { cmd ->
                    cmd.withName("INITIALIZATION-mongo-${UUID.randomUUID()}")
                }
                withReuse(TestcontainersConfiguration.getInstance().environmentSupportsReuse())
            }

            override fun start() {
                super.start()
                reset()
                initialize()
            }

            private fun reset() {
                deleteDatabase(environment.getRequiredProperty("spring.data.mongodb.database"))
            }

            private fun initialize() {
                // do whatever you need
            }
        }
}

private fun MongoDBContainer.deleteDatabase(name: String) {
    execInContainer("mongosh", name, "--eval='db.dropDatabase()'")
        .also {
            check(it.exitCode == 0) { "Drop database $name command failed ${it.stderr}." }
        }
}

package cz.gregersoftware.testcontainers.example.e6_environment.testcontainer

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

    @ServiceConnection
    @Bean
    fun mongoDBContainer(environment: Environment): MongoDBContainer =
        object : MongoDBContainer(DockerImageName.parse("mongo:latest")) {
            init {
                withCreateContainerCmdModifier { cmd ->
                    cmd.withName("ENVIRONMENT-mongo-${getProfilesString(environment).uppercase()}-${UUID.randomUUID()}")
                }
                withEnv("PROFILES", getProfilesString(environment))
                withReuse(TestcontainersConfiguration.getInstance().environmentSupportsReuse())
            }

            override fun start() {
                super.start()

                if (environment.activeProfiles.any { it.lowercase() == "test" }) {
                    reset()
                }
                initializeIfNeeded()
            }

            private fun reset() {
                deleteDatabase(environment.getRequiredProperty("spring.data.mongodb.database"))
            }

            private fun initializeIfNeeded() {
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

private fun getProfilesString(environment: Environment) =
    environment.activeProfiles.sorted().joinToString(",")

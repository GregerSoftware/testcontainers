package cz.gregersoftware.testcontainers.testcontainer

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.utility.TestcontainersConfiguration
import java.util.*

fun <T : GenericContainer<T>> GenericContainer<T>.applyGeneralConfiguration(name: String, environment: Environment): T =
    this
        .withCreateContainerCmdModifier { cmd ->
            cmd.withName("testcontainers-$name-${getProfilesString(environment).uppercase()}-${UUID.randomUUID()}")
        }
        .withNetworkAliases(name)
        .withLogConsumer { Slf4jLogConsumer(LoggerFactory.getLogger("$name-testcontainer")) }
        .withEnv("PROFILES", getProfilesString(environment))
        .withReuse(TestcontainersConfiguration.getInstance().environmentSupportsReuse())

private fun getProfilesString(environment: Environment) =
    environment.activeProfiles.sorted().joinToString(",")

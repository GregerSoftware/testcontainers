package cz.gregersoftware.testcontainers.testcontainer

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

@Import(RedisTestContainerConfiguration::class)
annotation class RedisTestContainer

@TestConfiguration(proxyBeanMethods = false)
private class RedisTestContainerConfiguration {

    @ServiceConnection(name = "redis")
    @Bean
    fun redisContainer(environment: Environment): GenericContainer<*> =
        GenericContainer(DockerImageName.parse("redis:latest"))
            .applyGeneralConfiguration("redis", environment)
            .withExposedPorts(6379)
}

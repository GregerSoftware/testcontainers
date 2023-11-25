package cz.gregersoftware.testcontainers.development

import cz.gregersoftware.testcontainers.configuration.DataLoader
import cz.gregersoftware.testcontainers.configuration.TestContainersConfiguration
import cz.gregersoftware.testcontainers.main
import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    System.setProperty("spring.profiles.active", "local")
    SpringApplication
        .from(::main)
        .with(
            TestContainersConfiguration::class.java,
            DataLoader::class.java
        )
        .run(*args)
}

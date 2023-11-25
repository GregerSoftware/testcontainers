package cz.gregersoftware.testcontainers.example.e6_environment.development

import org.springframework.boot.SpringApplication
import cz.gregersoftware.testcontainers.example.e6_environment.main
import org.springframework.context.annotation.ComponentScan

fun main(args: Array<String>) {
    System.setProperty("spring.profiles.active", "local")
    SpringApplication
            .from(::main)
            .with(TestContainersConfiguration::class.java)
            .run(*args)
}

@ComponentScan("cz.gregersoftware.testcontainers.example.e6_environment.testcontainer")
class TestContainersConfiguration

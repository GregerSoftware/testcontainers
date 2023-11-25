package cz.gregersoftware.testcontainers.example.e6_environment

import cz.gregersoftware.testcontainers.repository.ContainerRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = [ContainerRepository::class])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

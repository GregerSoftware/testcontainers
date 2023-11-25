package cz.gregersoftware.testcontainers.configuration

import cz.gregersoftware.testcontainers.repository.Container
import cz.gregersoftware.testcontainers.repository.ContainerRepository
import jakarta.annotation.PostConstruct

class DataLoader(
    private val repository: ContainerRepository
) {

    companion object {
        val MONGO_CONTAINER = Container(
            id = 1,
            image = "mongo",
            tag = "latest"
        )

        val KAFKA_CONTAINER = Container(
            id = 2,
            image = "kafka",
            tag = "latest"
        )

        private val CONTAINERS = listOf(MONGO_CONTAINER, KAFKA_CONTAINER)
    }

    @PostConstruct
    fun init() {
        if (repository.count() == 0L) {
            repository.saveAll(CONTAINERS)
        }
    }
}

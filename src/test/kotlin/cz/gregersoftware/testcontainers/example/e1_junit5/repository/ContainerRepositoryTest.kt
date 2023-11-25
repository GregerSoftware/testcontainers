package cz.gregersoftware.testcontainers.example.e1_junit5.repository

import cz.gregersoftware.testcontainers.example.BaseContainerRepositoryTest
import cz.gregersoftware.testcontainers.example.e1_junit5.testcontainer.MongoTestContainer
import cz.gregersoftware.testcontainers.repository.ContainerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.context.ImportTestcontainers
import org.testcontainers.junit.jupiter.Testcontainers

@ImportTestcontainers(MongoTestContainer::class)
@Testcontainers
class ContainerRepositoryTest @Autowired constructor(
        repository: ContainerRepository
) : BaseContainerRepositoryTest(repository)

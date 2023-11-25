package cz.gregersoftware.testcontainers.example.e5_initialization.repository

import cz.gregersoftware.testcontainers.example.BaseContainerRepositoryTest
import cz.gregersoftware.testcontainers.example.e5_initialization.testcontainer.MongoTestContainer
import cz.gregersoftware.testcontainers.repository.ContainerRepository
import org.springframework.beans.factory.annotation.Autowired

@MongoTestContainer
class ContainerRepositoryTest @Autowired constructor(
    repository: ContainerRepository
): BaseContainerRepositoryTest(repository)

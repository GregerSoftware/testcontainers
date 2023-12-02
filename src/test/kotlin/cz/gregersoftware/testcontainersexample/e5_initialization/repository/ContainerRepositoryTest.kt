package cz.gregersoftware.testcontainersexample.e5_initialization.repository

import cz.gregersoftware.testcontainersexample.BaseContainerRepositoryTest
import cz.gregersoftware.testcontainersexample.e5_initialization.testcontainer.MongoTestContainer
import cz.gregersoftware.testcontainers.repository.ContainerRepository
import org.springframework.beans.factory.annotation.Autowired

@MongoTestContainer
class ContainerRepositoryTest @Autowired constructor(
    repository: ContainerRepository
): BaseContainerRepositoryTest(repository)

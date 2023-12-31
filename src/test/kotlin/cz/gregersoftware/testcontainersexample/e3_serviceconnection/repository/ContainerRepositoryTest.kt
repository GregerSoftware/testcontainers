package cz.gregersoftware.testcontainersexample.e3_serviceconnection.repository

import cz.gregersoftware.testcontainersexample.BaseContainerRepositoryTest
import cz.gregersoftware.testcontainers.repository.ContainerRepository
import cz.gregersoftware.testcontainersexample.e3_serviceconnection.testcontainer.MongoTestContainer
import org.springframework.beans.factory.annotation.Autowired

@MongoTestContainer
class ContainerRepositoryTest @Autowired constructor(
    repository: ContainerRepository
): BaseContainerRepositoryTest(repository)

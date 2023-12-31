package cz.gregersoftware.testcontainersexample.e2_bean.repository

import cz.gregersoftware.testcontainersexample.BaseContainerRepositoryTest
import cz.gregersoftware.testcontainersexample.e2_bean.testcontainer.MongoTestContainer
import cz.gregersoftware.testcontainers.repository.ContainerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration

@MongoTestContainer
@ImportAutoConfiguration(TestcontainersPropertySourceAutoConfiguration::class)
class ContainerRepositoryTest @Autowired constructor(
        repository: ContainerRepository
) : BaseContainerRepositoryTest(repository)

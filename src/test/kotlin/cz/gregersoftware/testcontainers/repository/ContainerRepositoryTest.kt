package cz.gregersoftware.testcontainers.repository

import cz.gregersoftware.testcontainers.testcontainer.MongoTestContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@MongoTestContainer
@DataMongoTest
class ContainerRepositoryTest @Autowired constructor(
        private val repository: ContainerRepository
) {

    @AfterEach
    fun after() {
        repository.deleteAll()
    }

    @Test
    fun interaction() {
        val container = Container(
                id = 1,
                image = "mongo",
                tag = "latest"
        )
        assertThat(repository.findByIdOrNull(container.id)).isNull()
        assertThat(repository.save(container)).isNotNull
        assertThat(repository.findByIdOrNull(container.id)).isEqualTo(container)
    }
}

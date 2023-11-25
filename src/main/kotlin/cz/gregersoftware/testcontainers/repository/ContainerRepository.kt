package cz.gregersoftware.testcontainers.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ContainerRepository: MongoRepository<Container, Int>

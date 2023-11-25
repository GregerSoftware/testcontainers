package cz.gregersoftware.testcontainers.service

import cz.gregersoftware.testcontainers.repository.ContainerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ContainerService(
    private val repository: ContainerRepository
) {

    fun findById(id: Int) = repository.findByIdOrNull(id)
}

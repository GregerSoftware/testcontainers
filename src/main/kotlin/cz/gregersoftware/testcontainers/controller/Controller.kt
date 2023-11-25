package cz.gregersoftware.testcontainers.controller

import cz.gregersoftware.testcontainers.integration.kafka.CacheHitMessage
import cz.gregersoftware.testcontainers.integration.kafka.CacheHitMessageProducer
import cz.gregersoftware.testcontainers.integration.redis.CacheService
import cz.gregersoftware.testcontainers.integration.redis.CachedContainer
import cz.gregersoftware.testcontainers.repository.Container
import cz.gregersoftware.testcontainers.service.ContainerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/containers")
class Controller(
    private val service: ContainerService,
    private val cacheService: CacheService,
    private val cacheHitMessageProducer: CacheHitMessageProducer
) {

    @GetMapping("{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<ApiContainer> {
        fun getCachedContainer() = cacheService.findByIdOrNull(id)
            ?.also { cacheHitMessageProducer.send(mapToMessage(it)) }
            ?.let(::mapToApi)

        fun getContainerFromDatabase() = service.findById(id)
            ?.also { cacheService.save(mapToCache(it)) }
            ?.let(::mapToApi)

        return (getCachedContainer() ?: getContainerFromDatabase())
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    private fun mapToCache(container: Container) = CachedContainer(
        id = container.id,
        image = container.image,
        tag = container.tag,
        ttl = Instant.now().plusSeconds(5)
    )

    private fun mapToMessage(container: CachedContainer) = CacheHitMessage(
        id = container.id,
        time = Instant.now()
    )

    private fun mapToApi(container: CachedContainer) = ApiContainer(
        id = container.id,
        image = container.image,
        tag = container.tag,
        cacheInfo = ApiContainer.CacheInfo(
            ttl = container.ttl
        ),
    )

    private fun mapToApi(container: Container) = ApiContainer(
        id = container.id,
        image = container.image,
        tag = container.tag,
        cacheInfo = null
    )
}

package cz.gregersoftware.testcontainers.integration.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class CacheService(
    private val redisTemplate: RedisTemplate<Int, CachedContainer>
) {

    fun findByIdOrNull(id: Int) =
        redisTemplate.opsForValue().get(id)

    fun save(container: CachedContainer) =
        redisTemplate.opsForValue()
            .set(container.id, container, Duration.between(Instant.now(), container.ttl))
}

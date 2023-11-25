package cz.gregersoftware.testcontainers.integration.redis

import cz.gregersoftware.testcontainers.configuration.RedisConfiguration
import cz.gregersoftware.testcontainers.testcontainer.RedisTestContainer
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.time.Duration
import java.time.Instant

@ActiveProfiles("test")
@RedisTestContainer
@DataRedisTest
@Import(RedisConfiguration::class, CacheService::class)
class CacheServiceTest @Autowired constructor(
    private val cacheService: CacheService
) {

    @Test
    fun interaction() {
        val container = CachedContainer(
            id = 1,
            image = "redis",
            tag = "latest",
            ttl = Instant.now().plusSeconds(5)
        )

        assertThat(cacheService.findByIdOrNull(container.id)).isNull()
        assertThat(cacheService.save(container)).isNotNull
        assertThat(cacheService.findByIdOrNull(container.id)).isEqualTo(container)

        Awaitility.await()
            .atMost(Duration.ofSeconds(5))
            .until { cacheService.findByIdOrNull(container.id) == null }
    }
}

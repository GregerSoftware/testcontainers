package cz.gregersoftware.testcontainers.configuration

import cz.gregersoftware.testcontainers.integration.redis.CachedContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisConfiguration {

    @Bean
    fun redisTemplate(factory: RedisConnectionFactory) =
        RedisTemplate<Int, CachedContainer>()
            .apply { connectionFactory = factory }
}

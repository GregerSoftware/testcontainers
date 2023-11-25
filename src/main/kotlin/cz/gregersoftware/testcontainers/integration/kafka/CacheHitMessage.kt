package cz.gregersoftware.testcontainers.integration.kafka

import java.time.Instant

data class CacheHitMessage(
    val id: Int,
    val time: Instant
)

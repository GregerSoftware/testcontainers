package cz.gregersoftware.testcontainers.integration.redis

import java.io.Serializable
import java.time.Instant

data class CachedContainer(
    val id: Int,
    val image: String,
    val tag: String,
    val ttl: Instant
): Serializable

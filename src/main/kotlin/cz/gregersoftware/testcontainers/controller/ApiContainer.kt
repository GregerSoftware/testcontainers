package cz.gregersoftware.testcontainers.controller

import java.time.Instant

data class ApiContainer(
    val id: Int,
    val image: String,
    val tag: String,
    val cacheInfo: CacheInfo?
) {
    data class CacheInfo(
        val ttl: Instant
    )
}

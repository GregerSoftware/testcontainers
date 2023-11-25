package cz.gregersoftware.testcontainers.integration.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CacheHitMessageProducer(
    private val kafkaTemplate: KafkaTemplate<String, CacheHitMessage>
) {

    fun send(message: CacheHitMessage) {
        kafkaTemplate.send(TOPIC, message.id.toString(), message).join()
    }

    companion object {
        const val TOPIC = "containers-cache-hit"
    }
}

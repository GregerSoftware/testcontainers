package cz.gregersoftware.testcontainers.integration.kafka

import cz.gregersoftware.testcontainers.FullContextTest
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.test.utils.ContainerTestUtils
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference

class CacheHitMessageProducerTest @Autowired constructor(
    private val producer: CacheHitMessageProducer,
    private val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry
): FullContextTest() {

    private val receivedMessage = AtomicReference<CacheHitMessage?>(null)

    @Test
    fun interaction() {
        waitForTopicAssignment()

        val message = CacheHitMessage(
            id = 1,
            time = Instant.now()
        )

        producer.send(message)

        Awaitility.await()
            .atMost(Duration.ofSeconds(3))
            .until { receivedMessage.get() == message }
    }

    @KafkaListener(
        topics = [CacheHitMessageProducer.TOPIC],
        groupId = "test-group-id"
    )
    fun listen(record: ConsumerRecord<String, CacheHitMessage>) {
        receivedMessage.set(record.value())
    }

    private fun waitForTopicAssignment() {
        Awaitility.await()
            .atMost(Duration.ofSeconds(5))
            .until {
                kafkaListenerEndpointRegistry.listenerContainers
                    .forEach { ContainerTestUtils.waitForAssignment(it, 1) }
                true
            }
    }
}
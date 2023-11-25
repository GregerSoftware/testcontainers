package cz.gregersoftware.testcontainers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import cz.gregersoftware.testcontainers.configuration.DataLoader
import cz.gregersoftware.testcontainers.controller.ApiContainer
import cz.gregersoftware.testcontainers.integration.kafka.CacheHitMessage
import cz.gregersoftware.testcontainers.integration.kafka.CacheHitMessageProducer
import cz.gregersoftware.testcontainers.repository.Container
import cz.gregersoftware.testcontainers.repository.ContainerRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.awaitility.Awaitility
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference

@Import(DataLoader::class)
@AutoConfigureMockMvc
class E2ETest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry,
    private val containerRepository: ContainerRepository
) : FullContextTest() {

    private val objectMapper = jacksonObjectMapper().findAndRegisterModules()
    private val receivedMessages = AtomicReference(emptyList<CacheHitMessage>())

    @AfterEach
    fun after() {
        containerRepository.deleteAll()
    }

    @Test
    fun `e2e test`() {
        waitForTopicAssignment()

        checkContainerNotFound()

        val expectedMongoContainer = mapToApi(DataLoader.MONGO_CONTAINER)
        checkContainerLoadedFromDatabase(expectedMongoContainer)
        checkContainerLoadedFromCache(expectedMongoContainer)

        checkKafkaContainsMessages(expectedMongoContainer.id)

        val expectedKafkaContainer = mapToApi(DataLoader.KAFKA_CONTAINER)
        checkContainerLoadedFromDatabase(expectedKafkaContainer)
        checkContainerLoadedFromCache(expectedKafkaContainer)

        checkKafkaContainsMessages(expectedMongoContainer.id, expectedKafkaContainer.id)

        Thread.sleep(5000) // cache expiration
        checkContainerLoadedFromDatabase(expectedMongoContainer)
    }

    private fun mapToApi(databaseContainer: Container) = ApiContainer(
        id = databaseContainer.id,
        image = databaseContainer.image,
        tag = databaseContainer.tag,
        cacheInfo = null
    )

    private fun checkContainerNotFound() {
        mockMvc.get("/containers/{id}", 3)
            .andExpect {
                status { isNotFound() }
            }
    }

    private fun checkContainerLoadedFromDatabase(expected: ApiContainer) {
        mockMvc.get("/containers/{id}", expected.id)
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(objectMapper.writeValueAsString(expected)) }
            }
    }

    private fun checkContainerLoadedFromCache(expected: ApiContainer) {
        mockMvc.get("/containers/{id}", expected.id)
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("id", IsEqual(expected.id)) }
                content { jsonPath("cacheInfo.ttl", IsNot<Instant>(IsNull())) }
            }
    }

    private fun checkKafkaContainsMessages(vararg ids: Int) {
        Awaitility.await()
            .atMost(Duration.ofSeconds(3))
            .until {
                receivedMessages.get()
                    .map(CacheHitMessage::id)
                    .containsAll(ids.toList())
            }
    }

    @KafkaListener(
        topics = [CacheHitMessageProducer.TOPIC],
        groupId = "test-group-id",
        properties = [
            "spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer"
        ]
    )
    fun listen(record: ConsumerRecord<String, CacheHitMessage>) {
        receivedMessages.set(receivedMessages.get() + listOf(record.value()))
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

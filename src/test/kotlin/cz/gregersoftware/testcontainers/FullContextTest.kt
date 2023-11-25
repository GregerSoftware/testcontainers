package cz.gregersoftware.testcontainers

import cz.gregersoftware.testcontainers.configuration.TestContainersConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Import(TestContainersConfiguration::class)
@SpringBootTest
abstract class FullContextTest

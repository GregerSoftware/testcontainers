import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.spring") version "1.9.0"
}

group = "cz.greger-software"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

val testcontainers_version: String by extra // override spring bom version because of Kafka listener function

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers:$testcontainers_version")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainers_version")
    testImplementation("org.testcontainers:mongodb:$testcontainers_version")
    testImplementation("org.testcontainers:kafka:$testcontainers_version")
    testImplementation("org.awaitility:awaitility")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(20) // Java version
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "20"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

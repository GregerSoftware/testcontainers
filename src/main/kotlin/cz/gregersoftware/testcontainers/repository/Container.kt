package cz.gregersoftware.testcontainers.repository

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.FieldType
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collation = "containers")
data class Container(
    @MongoId(FieldType.STRING)
    val id: Int,
    val image: String,
    val tag: String
)

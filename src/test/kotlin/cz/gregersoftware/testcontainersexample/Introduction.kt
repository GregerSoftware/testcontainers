package cz.gregersoftware.testcontainersexample

import org.testcontainers.containers.Container
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.lifecycle.Startable
import org.testcontainers.utility.DockerImageName

/**
 * See [Startable], [Container], [GenericContainer], [MongoDBContainer]
 */
val mongoContainer: MongoDBContainer =
    MongoDBContainer(DockerImageName.parse("mongo:latest"))
//        .withEnv()
//        .withFileSystemBind()
//        .withCommand()
//        .withExposedPorts()
//        .withImagePullPolicy()

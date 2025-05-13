plugins {
    java
    alias(libs.plugins.shadow)
}

group = "org.cafiaso"
version = "1.0-SNAPSHOT"
description = "Cafiaso is a lightweight implementation of a 1.21.5 Minecraft server without using NMS"

repositories {
    mavenCentral()
}

dependencies {
    // Commons cli
    implementation(libs.commons.cli)

    // Guice
    implementation(libs.guice)

    // JSON
    implementation(libs.json)

    // Logging
    implementation(libs.slf4j.api)
    implementation(libs.slf4j.simple)

    // JUnit
    testImplementation(libs.junit.jupiter.engine)

    // Mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "com.cafiaso.server.Main"
        }
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveClassifier.set("")
    }

    test {
        useJUnitPlatform()
    }
}

import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish") version "0.30.0"
    id("com.gradleup.nmcp") version "0.0.7"
}

group = "com.developersyndicate.kraze"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.developersyndicate.kraze:kraze:1.0.0-alpha")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
    testImplementation(kotlin("test"))
}

mavenPublishing {
    coordinates("com.developersyndicate.kraze", "kraze-jackson", "1.0.0-alpha")

    pom {
        name.set("Kraze Jackson")
        description.set("Kraze Jackson is an extension for Kraze that integrates Jackson for JSON serialization and deserialization. It enables easy conversion between Kotlin objects and JSON in HTTP requests and responses, offering a type-safe and flexible solution to handle JSON with Kraze’s fluent API.")
        inceptionYear.set("2025")
        url.set("https://github.com/DeveloperSyndicate/Kraze")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("DeveloperSyndicate")
                name.set("Sanjay")
                url.set("https://github.com/DeveloperSyndicate/")
            }
        }
        scm {
            url.set("https://github.com/DeveloperSyndicate/Kraze/")
            connection.set("scm:git:git://github.com/DeveloperSyndicate/Kraze.git")
            developerConnection.set("scm:git:ssh://git@github.com/DeveloperSyndicate/Kraze.git")
        }
    }
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}

tasks.test {
    useJUnitPlatform()
}
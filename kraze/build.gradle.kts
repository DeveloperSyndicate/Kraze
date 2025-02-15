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
    api("com.squareup.okhttp3:logging-interceptor:4.12.0")
    testImplementation(kotlin("test"))
}

mavenPublishing {
    coordinates("com.developersyndicate.kraze", "kraze", "1.0.0-alpha")

    pom {
        name.set("Kraze")
        description.set("Kraze is a Kotlin-based, lightweight, and extensible HTTP networking library built on top of OkHttp. It provides a fluent API for making HTTP requests with support for serialization, authentication, and custom configuration.")
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
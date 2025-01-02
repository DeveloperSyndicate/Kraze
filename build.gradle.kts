plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.1.0"
}

group = "io.developersyndicate.kraze"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.jar {
    from(sourceSets.main.get().output)
}

dependencies {
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0-RC")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
}

group = "com.developersyndicate.kraze"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":kraze"))
    implementation(project(":kraze-jackson"))
    implementation(project(":kraze-gson"))
    implementation(project(":kraze-kotlinx-serialization"))
    implementation(project(":kraze-moshi"))
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
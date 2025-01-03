plugins {
    kotlin("jvm")
}

group = "io.developersyndicate.kraze"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":kraze"))
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
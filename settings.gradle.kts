plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "kraze-core"
include("kraze")
include("kraze-gson")
include("kraze-moshi")
include("kraze-kotlinx-serialization")
include("kraze-jackson")

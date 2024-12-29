plugins { kotlin("jvm") version "2.2.0" }

group = "kt2ts"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    implementation("io.github.kt2ts:kt2ts-annotation:1.0.0")
    implementation("io.github.kt2ts:kt2ts-annotation:1.0.0")
    implementation("org.yaml:snakeyaml:2.3")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.27.3")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(23) }

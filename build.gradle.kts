plugins { kotlin("jvm") version "2.0.21" }

group = "kt2ts"

version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://mlorber.net/maven_repo")
}

dependencies {
    implementation("io.github.kt2ts:kt2ts-annotation:1.0.0")
    implementation("org.yaml:snakeyaml:2.3")

    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(21) }

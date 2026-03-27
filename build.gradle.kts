plugins { kotlin("jvm") version "2.2.0" }

group = "kt2ts"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("com.google.code.gson:gson:2.13.1")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(23) }

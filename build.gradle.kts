plugins { kotlin("jvm") version "2.2.0" }

group = "kt2ts"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    implementation("io.github.tree-sitter:ktreesitter:0.24.1")
    implementation("io.github.bonede:tree-sitter-kotlin:0.3.8.1")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.27.3")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(23) }

package kt2ts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.file.Path
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class Kt2TsConverterTest {

    data class ExampleTestCase(
        val name: String,
        val inputKotlin: String,
        val expectedTypeScript: String,
        val configuration: Kt2TsConfiguration,
    ) {
        override fun toString(): String = name
    }

    companion object {
        private val gson = Gson()

        @JvmStatic
        fun exampleTestCases(): List<ExampleTestCase> {
            val examplesDir =
                Path.of(
                    Kt2TsConverterTest::class
                        .java
                        .classLoader
                        .getResource("examples")!!
                        .path
                )
            return examplesDir
                .toFile()
                .listFiles()!!
                .filter { it.isDirectory }
                .sorted()
                .map { dir ->
                    val inputFile = dir.resolve("input.kt")
                    val expectedFile = dir.resolve("expected.ts")
                    require(inputFile.exists()) { "Missing input.kt in ${dir.name}" }
                    require(expectedFile.exists()) { "Missing expected.ts in ${dir.name}" }

                    val configuration = buildConfiguration(dir)

                    ExampleTestCase(
                        name = dir.name,
                        inputKotlin = inputFile.readText(),
                        expectedTypeScript = expectedFile.readText().trimEnd(),
                        configuration = configuration,
                    )
                }
        }

        private fun buildConfiguration(dir: java.io.File): Kt2TsConfiguration {
            val mappingsFile = dir.resolve("mappings.json")
            val customMappings =
                if (mappingsFile.exists()) {
                    val type = object : TypeToken<Map<String, String>>() {}.type
                    gson.fromJson<Map<String, String>>(mappingsFile.readText(), type)
                } else {
                    emptyMap()
                }

            val configFile = dir.resolve("config.json")
            val (nominalStringMappings, mapClass) =
                if (configFile.exists()) {
                    @Suppress("UNCHECKED_CAST")
                    val raw =
                        gson.fromJson(configFile.readText(), Map::class.java) as Map<String, Any>
                    val nominals =
                        (raw["nominalStringMappings"] as? List<*>)
                            ?.filterIsInstance<String>()
                            ?.toSet() ?: emptySet()
                    val map = raw["mapClass"] as? String ?: "Record"
                    nominals to map
                } else {
                    emptySet<String>() to "Record"
                }

            return Kt2TsConfiguration(
                customMappings = customMappings,
                nominalStringMappings = nominalStringMappings,
                mapClass = mapClass,
            )
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("exampleTestCases")
    fun `convert Kotlin to TypeScript`(testCase: ExampleTestCase) {
        val actual = Kt2TsConverter.convert(testCase.inputKotlin, testCase.configuration).trimEnd()
        assertThat(actual)
            .describedAs("Conversion mismatch for ${testCase.name}")
            .isEqualTo(testCase.expectedTypeScript)
    }
}

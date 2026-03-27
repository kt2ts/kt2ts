package kt2ts

/**
 * Configuration for the Kotlin-to-TypeScript conversion.
 *
 * @param customMappings Fully-qualified Kotlin class name → TypeScript type string.
 *        Example: `"java.time.LocalDate" to "string"`.
 * @param nominalStringMappings Fully-qualified base class names whose subclasses
 *        should be converted to `NominalString<'ClassName'>`.
 * @param mapClass The TypeScript type to use for `Map<K,V>` (default: `"Record"`).
 */
data class Kt2TsConfiguration(
    val customMappings: Map<String, String> = emptyMap(),
    val nominalStringMappings: Set<String> = emptySet(),
    val mapClass: String = "Record",
)

/**
 * Converts Kotlin source code to TypeScript declarations.
 *
 * Takes raw Kotlin source as a string, parses it into an AST,
 * and produces the corresponding TypeScript output.
 */
object Kt2TsConverter {

    fun convert(
        kotlinSource: String,
        configuration: Kt2TsConfiguration = Kt2TsConfiguration(),
    ): String {
        TODO("AST-based conversion not yet implemented")
    }
}

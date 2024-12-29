package kt2ts.domain

import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

// TODO recup from template-cloner for the moment
data class Configuration(
    val cloneSourcePath: Path,
    val cloneSourceBranch: String,
    val cloneDestinationPath: Path,
    val cloneDestinationBranch: String,
    val versionFilename: String,
    val cloneWorkingDirectory: Path,
    val filteredFields: List<String>,
    val generationStrategy: GenerationStrategy,
    val postGenerate: List<String>,
    val keepCaches: List<String>,
    val exclusions: List<String>,
    val transformExclusions: List<String>,
    val userPath: String,
    val commitMessagePrefix: String,
    val templateMap: Map<String, String>,
    val defaultsLog: Map<String, String>,
    val debug: Boolean,
) {

    enum class GenerationStrategy {
        LastCommit,
        AllCommits,
        Reverse
    }

    companion object {
        val templatePrefix = "template."
        val filterPrefix = ":"
        val tempDirectoryPrefix = "template-cloner"

        val defaultVersionFilename = ".template/version"
        val defaultFilteredFields = "label"

        fun load(configurationFile: File): Configuration {
            if (!configurationFile.exists()) {
                throw IllegalArgumentException("${configurationFile.absolutePath} doesn't exist")
            }
            return YamlParser.yamlToMap(
                FileInputStream(configurationFile),
                )
                .let { properties ->
                    val defaultsLog = mutableMapOf<String, String>()
                    Configuration(
                        cloneSourcePath =
                            Paths.get(getProperty(properties, "clone.source.path", defaultsLog)),
                        cloneSourceBranch =
                            getProperty(properties, "clone.source.branch", defaultsLog),
                        cloneDestinationPath =
                            Paths.get(
                                getProperty(properties, "clone.destination.path", defaultsLog)),
                        cloneDestinationBranch =
                            getProperty(properties, "clone.destination.branch", defaultsLog),
                        versionFilename =
                            getProperty(properties, "clone.version-filename", defaultsLog) {
                                defaultVersionFilename
                            },
                        cloneWorkingDirectory =
                            Paths.get(
                                getProperty(
                                    properties, "clone.clone-working-directory", defaultsLog) {
                                        Files.createTempDirectory(tempDirectoryPrefix)
                                            .absolutePathString()
                                    }),
                        filteredFields =
                            getProperty(properties, "clone.filtered-fields", defaultsLog) {
                                    defaultFilteredFields
                                }
                                .split(","),
                        generationStrategy =
                            getProperty(properties, "clone.generate-strategy", defaultsLog).let {
                                when (it) {
                                    "last-commit" -> LastCommit
                                    "all-commits" -> AllCommits
                                    "reverse" -> Reverse
                                    else ->
                                        throw RuntimeException(
                                            "Configuration generate-strategy value is \"$it\", should be : [${values()}]")
                                }
                            },
                        postGenerate =
                            YamlParser.getListByKeyPrefix(properties, "clone.post-generate"),
                        keepCaches = YamlParser.getListByKeyPrefix(properties, "clone.keep-caches"),
                        exclusions =
                            getProperty(properties, "clone.exclusions", defaultsLog).let {
                                it?.split(",")?.map { it.trim() } ?: emptyList()
                            },
                        transformExclusions =
                            getProperty(properties, "clone.transformExclusions", defaultsLog).let {
                                it?.split(",")?.map { it.trim() } ?: emptyList()
                            },
                        userPath = getProperty(properties, "clone.userPath", defaultsLog),
                        commitMessagePrefix =
                            getProperty(properties, "clone.commit-message-prefix", defaultsLog) {
                                ""
                            },
                        templateMap =
                            properties.keys
                                .filter { it.startsWith(templatePrefix) }
                                .associate {
                                    it.removePrefix(templatePrefix) to properties.getValue(it)
                                },
                        defaultsLog = defaultsLog,
                        debug =
                            getProperty(properties, "clone.debug", defaultsLog) { false.toString() }
                                .let { it == "true" },
                    )
                }
                .let {
                    if (it.generationStrategy == Reverse) {
                        it.copy(
                            cloneSourcePath = it.cloneDestinationPath,
                            cloneSourceBranch = it.cloneDestinationBranch,
                            cloneDestinationPath = it.cloneSourcePath,
                            cloneDestinationBranch = it.cloneSourceBranch)
                    } else {
                        it
                    }
                }
        }

        fun getProperty(
            properties: Map<String, String>,
            key: String,
            defaultsLog: MutableMap<String, String>,
            default: (() -> String)? = null
        ): String =
            properties[key]
                ?: if (default != null) {
                    default().also { defaultsLog[key] = it }
                } else {
                    throw IllegalStateException("Missing configuration key $key")
                }
    }

    private val filters =
        listOf(
            "lowerNoSpace" to { s: String -> s.lowercase().replace(" ", "") },
            "lowerCamelCase" to
                { s: String ->
                    s.split(" ")
                        .map { it.replaceFirstChar { it.uppercase() } }
                        .joinToString(separator = "")
                        .replaceFirstChar { it.lowercase() }
                },
            "upperCamelCase" to
                { s: String ->
                    s.split(" ")
                        .map { it.replaceFirstChar { it.uppercase() } }
                        .joinToString(separator = "")
                },
            "upperSnakeCase" to
                { s: String ->
                    s.split(" ").map { it.uppercase() }.joinToString(separator = "_")
                },
            "lowerKebabCase" to { s: String -> s.lowercase().replace(" ", "-") },
        )

    fun replacementList(): List<Pair<String, String>> =
        templateMap.entries
            .flatMap { (key, value) ->
                listOf(key to value).let {
                    it +
                        if (key in filteredFields) {
                            filters
                                .map { (filterKey, filter) ->
                                    "$key$filterPrefix$filterKey" to filter(value)
                                }
                                .let {
                                    if (generationStrategy == Reverse) it.distinctBy { it.second }
                                    else it
                                }
                        } else emptyList()
                }
            }
            .map { "[${it.first}]" to it.second }
            // TODO doc and test, replaces com.fmkbase before fmkbase
            .sortedByDescending { it.second.length }
            .let { if (generationStrategy == Reverse) it.map { it.second to it.first } else it }
}
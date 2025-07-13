package kt2ts.resolve

import java.io.File
import java.nio.file.Path
import kotlin.io.path.pathString

object KotlinSourceFilesResolver {

    val KotlinExtension = "kt"

    // TODO [conf] can change from config
    // can use regex ?
    val DefaultIgnoreList =
        listOf(
            "node_modules",
            ".git",
            ".gradle",
            ".devbox",
            ".venv",
            ".idea",
            ".yarn",
            ".terraform",
        )
    val ExtensionIgnoreList = listOf("jar", "war", "original", "RData", "sql")

    fun sequenceKotlinFiles(dir: Path, onlyKotlinFiles: Boolean) =
        dir.toFile()
            .walk()
            .onEnter { filterDir(it, dir) }
            .filter { !onlyKotlinFiles || it.extension == KotlinExtension }
            .filter { it.extension !in ExtensionIgnoreList }
            .filter { !it.isDirectory }

    fun filterDir(dir: File, root: Path): Boolean {
        assert(dir.isDirectory)
        if (dir.name in DefaultIgnoreList) return false
        return !isBuildDir(relativePath(dir, root))
    }

    // TODO [conf] can disable build directory behaviour
    fun isBuildDir(relativePath: String): Boolean {
        assert(relativePath.isEmpty() || relativePath.first() == '/')
        return if (relativePath.endsWith("/build")) {
            relativePath.indexOf("/src/") == -1
        } else {
            false
        }
    }

    fun relativePath(file: File, root: Path): String = file.path.substring(root.pathString.length)
}

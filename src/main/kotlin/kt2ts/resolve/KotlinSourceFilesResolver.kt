package kt2ts.resolve

import java.io.File
import java.nio.file.Path
import kotlin.io.path.pathString

object KotlinSourceFilesResolver {

    val KotlinExtension = "kt"

    // TODO [conf] can change from config
    // can use regex ?
    val DefaultIgnoreList = listOf("node_modules", ".git", ".gradle")

    fun sequenceKotlinFiles(
        dir: Path,
        filterBuildDir: Boolean,
        respectIgnoreList: Boolean,
        filterExtension: Boolean,
    ) =
        dir.toFile()
            .walk()
            .onEnter {
                filterDir(
                    dir = it,
                    root = dir,
                    filterBuildDir = filterBuildDir,
                    respectIgnoreList = respectIgnoreList,
                )
            }
            .filter { !filterExtension || it.extension == KotlinExtension }
            .filter { !it.isDirectory }

    fun filterDir(
        dir: File,
        root: Path,
        filterBuildDir: Boolean,
        respectIgnoreList: Boolean,
    ): Boolean {
        assert(dir.isDirectory)
        if (respectIgnoreList && dir.name in DefaultIgnoreList) return false
        return !filterBuildDir || !isBuildDir(relativePath(dir, root))
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

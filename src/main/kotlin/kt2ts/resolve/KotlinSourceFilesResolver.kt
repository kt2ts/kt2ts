package kt2ts.resolve

import java.io.File
import java.nio.file.Path
import kotlin.io.path.pathString

// TODO more generic, et ranger avec mon shellrunner ?
object KotlinSourceFilesResolver {

    val KotlinExtension = "kt"

    // TODO [conf] can change from config
    // can use regex ?
    // TODO set
    val DefaultIgnoreList = listOf("node_modules", ".git", ".gradle")

    fun sequenceKotlinFiles(dir: Path) =
        dir.toFile()
            .walk()
            .onEnter { filterDir(it, dir) }
            // dans lite i did .filter { extensionsWhileList != null && it.extension in extensionsWhileList }
            // avec extensionsWhileList en param : extensionsWhileList: Set<String>? = null
            .filter { it.extension == KotlinExtension }
            .filter { !it.isDirectory }

    fun filterDir(dir: File, root: Path): Boolean {
        assert(dir.isDirectory)
        if (dir.name in DefaultIgnoreList) return false
        return !isBuildDir(relativePath(dir, root))
    }

    // TODO [conf] can disable build directory behaviour
    // vraiment pas ouf
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

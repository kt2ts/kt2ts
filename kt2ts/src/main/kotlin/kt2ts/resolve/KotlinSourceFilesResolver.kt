package kt2ts.resolve

import java.io.File
import java.nio.file.Path
import kt2ts.domain.ClassQualifiedName
import kt2ts.domain.KotlinFile
import kt2ts.domain.PackageName
import kt2ts.domain.SourceFiles

object KotlinSourceFilesResolver {

    val PackageDeclaration = "package "
    val ImportDeclaration = "import "

    fun resolveDirectory(dir: Path, searchAnnotation: ClassQualifiedName): SourceFiles {
        // first is for initial selection, second is for the rest
        val files: Pair<List<KotlinFile?>, List<KotlinFile?>> =
            dir.toFile()
                .walk()
                .filter { it.extension == "kt" }
                .mapNotNull {
                    val packageAndImports = readPackageAndImports(it)
                    val packageName =
                        packageAndImports.firstOrNull()?.let { PackageName(it) }
                            // TODO Kotlin with no package is ok ?
                            ?: return@mapNotNull null
                    val ktFile = KotlinFile(it.toPath(), packageName)
                    // return Pair<KotlinFile, KotlinFile>, first is for initial selection, second
                    // is for the rest
                    if (searchAnnotation.name in packageAndImports) ktFile to null
                    else null to ktFile
                }
                .unzip()
        return SourceFiles(
            files.first.filterNotNull(),
            files.second.filterNotNull().groupBy { it.packageName },
        )
    }

    // TODO test
    private fun readPackageAndImports(file: File): List<String> =
        file.useLines {
            it
                // (sequential processing)
                .map {
                    val line = it.trim()
                    val (shouldContinueReading, extract) =
                        when {
                            line.isEmpty() -> true to null
                            line.startsWith(PackageDeclaration) ->
                                true to line.substring(PackageDeclaration.length)
                            line.startsWith(ImportDeclaration) ->
                                true to line.substring(ImportDeclaration.length)
                            // if not empty, not package declaration & not import declaration, then
                            // we've reached the end of imports
                            else -> false to null
                        }
                    shouldContinueReading to extract
                }
                .takeWhile { it.first }
                .mapNotNull { it.second }
                .toList()
        }
}

package kt2ts.resolve

import java.io.File
import java.nio.file.Path
import kt2ts.domain.ClassQualifiedName
import kt2ts.domain.KotlinFile
import kt2ts.domain.PackageName
import kt2ts.domain.SourceFiles
import kt2ts.resolve.KotlinSourceFilesResolver.sequenceKotlinFiles

object KotlinSelectionResolver {

    val PackageDeclaration = "package "
    val ImportDeclaration = "import "

    fun resolveDirectory(dir: Path, searchAnnotation: ClassQualifiedName): SourceFiles =
        sequenceKotlinFiles(dir)
            .mapNotNull {
                val packageAndImports = readPackageAndImports(it)
                val packageName =
                    packageAndImports.firstOrNull()?.let { PackageName(it) }
                    // TODO Kotlin with no package is ok ?
                        ?: return@mapNotNull null
                val ktFile = KotlinFile(it.toPath(), packageName)
                // return Pair<KotlinFile, KotlinFile>, first is for initial selection, second
                // is for the rest
                if (searchAnnotation.name in packageAndImports) ktFile to null else null to ktFile
            }
            // first is for initial selection, second is for the rest
            .unzip()
            .let { SourceFiles(it.first.filterNotNull(), it.second.filterNotNull()) }

    // reads just needed lines
    fun readPackageAndImports(file: File): List<String> =
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
                            // if not empty, not package declaration & not import declaration,
                            // then
                            // we've reached the end of imports
                            else -> false to null
                        }
                    shouldContinueReading to extract
                }
                .takeWhile { it.first }
                .mapNotNull { it.second }
                .toList()
        }

    // reads all file
    fun readPackageAndImportsFull(file: File): List<String> =
        file.useLines {
            it.toList().mapNotNull {
                val line = it.trim()
                when {
                    line.startsWith(PackageDeclaration) -> line.substring(PackageDeclaration.length)
                    line.startsWith(ImportDeclaration) -> line.substring(ImportDeclaration.length)
                    // if not empty, not package declaration & not import declaration, then
                    // we've reached the end of imports
                    else -> null
                }
            }
        }
}

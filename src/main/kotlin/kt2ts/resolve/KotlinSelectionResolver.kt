package kt2ts.resolve

import java.io.File

object KotlinSelectionResolver {

    val PackageDeclaration = "package "
    val ImportDeclaration = "import "

    // reads just needed lines
    fun readPackageAndImports(file: File): List<String> {
        val start = System.currentTimeMillis()
        val r =
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
        val time = System.currentTimeMillis() - start
        if (time > 50) {
            println("readPackageAndImportsFull(${file.path}) took ${time}ms")
        }
        return r
    }

    // reads all file
    fun readPackageAndImportsFull(file: File): List<String> {
        val start = System.currentTimeMillis()
        val r =
            file.useLines {
                it.toList().mapNotNull {
                    val line = it.trim()
                    when {
                        line.startsWith(PackageDeclaration) ->
                            line.substring(PackageDeclaration.length)
                        line.startsWith(ImportDeclaration) ->
                            line.substring(ImportDeclaration.length)
                        // if not empty, not package declaration & not import declaration, then
                        // we've reached the end of imports
                        else -> null
                    }
                }
            }
        val time = System.currentTimeMillis() - start
        if (time > 50) {
            println("readPackageAndImportsFull(${file.path}) took ${time}ms")
        }
        return r
    }
}

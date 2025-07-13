package kt2ts.resolve

import kotlin.io.path.Path
import org.junit.jupiter.api.Test

class KotlinSelectionResolverPerfTest {

    @Test
    fun `test perf about read all file vs only package & imports lines`() {
        val gitDir = Path(System.getenv("HOME")).resolve("git")
        time("Kotlin files only, read only needed lines") {
            KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir, true).map {
                KotlinSelectionResolver.readPackageAndImports(it)
            }
        }
        time("Kotlin files only, read all lines") {
            KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir, true).map {
                KotlinSelectionResolver.readPackageAndImportsFull(it)
            }
        }
        time("All files, read only needed lines") {
            KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir, false).map {
                KotlinSelectionResolver.readPackageAndImports(it)
            }
        }
        time("All files, read all lines") {
            KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir, false).map {
                KotlinSelectionResolver.readPackageAndImportsFull(it)
            }
        }
    }

    fun time(m: String, run: () -> Sequence<*>) {
        val start = System.currentTimeMillis()
        val count = run().count()
        println(
            "${m.padEnd(42)} ${(System.currentTimeMillis() - start).toString().padStart(7)}ms " +
                "for ${count.toString().padStart(7)} files"
        )
    }
}

package kt2ts.resolve

import kotlin.io.path.Path
import kotlin.test.Test
import kt2ts.resolve.KotlinSourceFilesResolver.sequenceKotlinFiles

class KotlinSourceFilesResolverPerfTest {

    @Test
    fun `test sequenceKotlinFiles performance`() {
        val gitDir = Path(System.getenv("HOME")).resolve("git/kt2ts")
        val filterExtension = true
        time("full shortcuts") {
            sequenceKotlinFiles(
                dir = gitDir,
                filterBuildDir = true,
                respectIgnoreList = true,
                filterExtension = filterExtension,
            )
        }
        time("no /build dir filter") {
            sequenceKotlinFiles(
                dir = gitDir,
                filterBuildDir = false,
                respectIgnoreList = true,
                filterExtension = filterExtension,
            )
        }
        time("no ignore list") {
            sequenceKotlinFiles(
                dir = gitDir,
                filterBuildDir = true,
                respectIgnoreList = false,
                filterExtension = filterExtension,
            )
        }
    }

    fun time(m: String, run: () -> Sequence<*>) {
        val start = System.currentTimeMillis()
        val count = run().count()
        println(
            "${m.padEnd(20)} ${(System.currentTimeMillis() - start).toString().padStart(7)}ms " +
                "for ${count.toString().padStart(7)} files"
        )
    }
}

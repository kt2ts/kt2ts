package kt2ts.resolve

import kotlin.io.path.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test

class FileOpenPerfTest {

    @Test
    fun `test perfs`() {
        val gitDir = Path(System.getenv("HOME")).resolve("git")
        time("coroutines") {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val readSemaphore = Semaphore(permits = 4)
                    KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir).forEach { file ->
                        launch { readSemaphore.withPermit { file.readBytes() } }
                    }
                }
            }
        }
    }

    fun time(m: String, run: () -> Any) {
        val start = System.currentTimeMillis()
        println("${m.padEnd(42)} ${(System.currentTimeMillis() - start).toString().padStart(7)}ms")
    }
}

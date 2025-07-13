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
        time("No coroutine") {
            KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir).forEach { file ->
                file.readBytes()
            }
        }
        time("1 coroutine") {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val readSemaphore = Semaphore(permits = 1)
                    KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir).forEach { file ->
                        launch { readSemaphore.withPermit { file.readBytes() } }
                    }
                }
            }
        }
        time("4 coroutines") {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val readSemaphore = Semaphore(permits = 4)
                    KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir).forEach { file ->
                        launch { readSemaphore.withPermit { file.readBytes() } }
                    }
                }
            }
        }
        time("10 coroutines") {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val readSemaphore = Semaphore(permits = 10)
                    KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir).forEach { file ->
                        launch { readSemaphore.withPermit { file.readBytes() } }
                    }
                }
            }
        }
        time("20 coroutines") {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val readSemaphore = Semaphore(permits = 20)
                    KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir).forEach { file ->
                        launch { readSemaphore.withPermit { file.readBytes() } }
                    }
                }
            }
        }
        time("No coroutine") {
            KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir).forEach { file ->
                file.readBytes()
            }
        }
    }

    fun time(m: String, run: () -> Any) {
        val start = System.currentTimeMillis()
        run()
        println("${m.padEnd(42)} ${(System.currentTimeMillis() - start).toString().padStart(7)}ms")
    }
}

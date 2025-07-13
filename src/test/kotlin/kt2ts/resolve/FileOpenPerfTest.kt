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
        val start = System.currentTimeMillis()
        runBlocking {
            val readSemaphore = Semaphore(permits = 4)
            withContext(Dispatchers.IO) {
                KotlinSourceFilesResolver.sequenceKotlinFiles(gitDir).forEach { file ->
                    launch { readSemaphore.withPermit { file.readBytes() } }
                }
            }
        }
        println("finished in ${System.currentTimeMillis() - start} ms")
    }
}

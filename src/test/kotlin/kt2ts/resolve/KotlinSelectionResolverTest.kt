package kt2ts.resolve

import java.nio.file.Path
import kotlin.io.path.name
import kotlin.test.Test
import kt2ts.annotation.GenerateTypescript
import kt2ts.domain.ClassQualifiedName
import kt2ts.domain.PackageName
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class KotlinSelectionResolverTest {
    @Test
    fun `should resolve kotlin source`() {
        val dir =
            Path.of(
                KotlinSourceFilesResolverTest::class
                    .java
                    .classLoader
                    .getResource("sample-kotlin/io/github/kt2tssample")!!
                    .path
            )
        // TODO configurable
        val annotation = ClassQualifiedName.of(GenerateTypescript::class)
        // TODO name parse
        val resolution = KotlinSelectionResolver.resolveDirectory(dir, annotation)
        // TODO use kotest
        assertEquals(3, resolution.annotatedFiles.size)
        resolution.annotatedFiles
            .sortedBy { it.path.name }
            .let {
                assertEquals("Classes.kt", it[0].path.name)
                assertEquals("Sealed.kt", it[1].path.name)
                assertEquals("SubPackageClasses.kt", it[2].path.name)
            }
        assertEquals(4, resolution.otherFiles.size)
        assertEquals(2, resolution.otherFiles.map { it.packageName }.distinct().size)
        resolution.otherFiles
            .filter { it.packageName == PackageName("io.github.kt2tssample") }
            .sortedBy { it.path.name }
            .let {
                assertEquals(3, it.size)
                assertEquals("Enum.kt", it[0].path.name)
                assertEquals("NoGeneration.kt", it[1].path.name)
                assertEquals("SampleId.kt", it[2].path.name)
            }
        resolution.otherFiles
            .filter { it.packageName == PackageName("io.github.kt2tssample.subpackage") }
            .let {
                assertEquals(1, it.size)
                assertEquals("NoGenerationInSubPackage.kt", it[0].path.name)
            }
    }
}

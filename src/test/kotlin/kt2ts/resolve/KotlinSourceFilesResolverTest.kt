package kt2ts.resolve

import java.nio.file.Path
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

class KotlinSourceFilesResolverTest {

    @Test
    fun `test sequenceKotlinFiles()`() = assertSoftly {
        val dir =
            Path.of(
                KotlinSourceFilesResolverTest::class
                    .java
                    .classLoader
                    .getResource("KotlinSourceFilesResolverTest-samples")!!
                    .path
            )
        val kotlinFiles =
            KotlinSourceFilesResolver.sequenceKotlinFiles(dir)
                .map { it.toPath() }
                .toList()
                .map { KotlinSourceFilesResolver.relativePath(it.toFile(), dir) }
                .sorted()
        // DefaultIgnoreList is tested with a "node_modules" dir only
        assertThat(kotlinFiles)
            .containsExactlyInAnyOrder(
                "/src/File.kt",
                "/src/build/File.kt",
                "/src/filterdir.kt/File.kt",
                "/test/File.kt",
            )
    }

    @Test
    fun `test filterDir()`() = assertSoftly {
        val dir =
            Path.of(
                KotlinSourceFilesResolverTest::class
                    .java
                    .classLoader
                    .getResource("KotlinSourceFilesResolverTest-samples")!!
                    .path
            )
        it.assertThat(KotlinSourceFilesResolver.filterDir(dir.toFile(), dir)).isTrue
        // stopping tests here, test for sequenceKotlinFiles() is more exhaustive, but this test
        // ensures relativePath() result starts with a slash
        it.assertThat(KotlinSourceFilesResolver.filterDir(dir.resolve("build").toFile(), dir))
            .isFalse
    }

    @Test
    fun `test isBuildDir()`() = assertSoftly {
        // do not ends with /build
        it.assertThat(KotlinSourceFilesResolver.isBuildDir("/foo/bar")).isFalse
        it.assertThat(KotlinSourceFilesResolver.isBuildDir("/foo/build/bar")).isFalse

        // ends with /build, not in /src/
        it.assertThat(KotlinSourceFilesResolver.isBuildDir("/foo/build")).isTrue
        it.assertThat(KotlinSourceFilesResolver.isBuildDir("/foo/test/build")).isTrue

        // ends with /build but is in /src/
        it.assertThat(KotlinSourceFilesResolver.isBuildDir("/foo/src/build")).isFalse
        it.assertThat(KotlinSourceFilesResolver.isBuildDir("/foo/src/test/build")).isFalse
    }

    @Test
    fun `test relativePath()`() {
        val dir =
            Path.of(KotlinSourceFilesResolver::class.java.classLoader.getResource("kotlin")!!.path)
        val file =
            dir.resolve(
                    KotlinSourceFilesResolverTest::class.qualifiedName!!.replace(".", "/") + ".kt"
                )
                .parent
        val relativePath = KotlinSourceFilesResolver.relativePath(file.toFile(), dir)
        assertThat(relativePath.startsWith("/")).isTrue.`as` {
            "Relative path should start with a slash because of ${KotlinSourceFilesResolver::isBuildDir}"
        }
        assertThat(relativePath).isEqualTo("/kt2ts/resolve")
    }
}

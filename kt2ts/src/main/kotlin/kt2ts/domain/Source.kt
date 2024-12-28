package kt2ts.domain

import java.nio.file.Path

data class KotlinFile(val path: Path, val packageName: PackageName)

data class SourceFiles(
    val initialSelection: List<KotlinFile>,
    val otherFiles: Map<PackageName, List<KotlinFile>>,
)

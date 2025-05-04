package kt2ts.domain

import java.nio.file.Path

data class KotlinFile(val path: Path, val packageName: PackageName)

data class SourceFiles(val annotatedFiles: List<KotlinFile>, val otherFiles: List<KotlinFile>)

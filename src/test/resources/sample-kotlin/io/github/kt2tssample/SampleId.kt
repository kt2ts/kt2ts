package io.github.kt2tssample

import java.util.UUID

interface SampleId<T> {
    val rawId: T
}

abstract class SampleUuidId : SampleId<UUID>

data class MySampleId(override val rawId: UUID) : SampleUuidId()

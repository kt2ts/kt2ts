package cookbook

import java.time.LocalDate
import java.time.Instant
import java.util.UUID

data class RecipeEvent(
    val id: UUID,
    val createdAt: Instant,
    val scheduledDate: LocalDate,
    val description: String,
)

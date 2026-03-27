package cookbook

data class Ingredient(
    val name: String,
    val quantity: Double?,
    val unit: String?,
    val notes: String?,
    val organic: Boolean,
)

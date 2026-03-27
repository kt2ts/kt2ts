package cookbook

data class Recipe(
    val title: String,
    val description: String,
    val preparationMinutes: Int,
    val cookingMinutes: Int,
    val servings: Int,
    val rating: Double,
    val vegetarian: Boolean,
)

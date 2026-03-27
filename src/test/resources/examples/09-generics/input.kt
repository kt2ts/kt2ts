package cookbook

data class PortionOf<T>(
    val content: T,
    val quantity: Double,
    val unit: String,
)

data class RecipeCollection(
    val byCategory: Map<String, List<Recipe>>,
    val pairings: List<Pair<Recipe, Recipe>>,
)

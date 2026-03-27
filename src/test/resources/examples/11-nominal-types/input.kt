package cookbook

abstract class CulinaryId(open val rawId: String)

data class RecipeId(override val rawId: String) : CulinaryId(rawId)
data class IngredientId(override val rawId: String) : CulinaryId(rawId)
data class ChefId(override val rawId: String) : CulinaryId(rawId)

data class RecipeCard(
    val id: RecipeId,
    val chefId: ChefId,
    val title: String,
    val ingredients: List<IngredientId>,
)

package cookbook

data class FullRecipe(
    val id: RecipeId,
    val title: String,
    val chef: Chef,
    val mealType: MealType,
    val cookingMethod: CookingMethod,
    val ingredients: List<Ingredient>,
    val tags: Set<String>,
    val nutritionPerServing: Map<String, Double>,
    val relatedRecipes: List<RecipeId>?,
    val servings: Int,
    val vegetarian: Boolean,
)

package cookbook

data class Cookbook(
    val title: String,
    val recipes: List<Recipe>,
    val tags: Set<String>,
    val pageNumbers: List<Int>,
)

data class Pantry(
    val stock: Map<String, Int>,
    val prices: Map<String, Double>,
)

data class CookingRange(
    val temperatureRange: Pair<Int, Int>,
    val timeRange: Pair<Int, Int>,
)

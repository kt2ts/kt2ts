package cookbook

data class WeeklyMenu(
    val days: List<String>,
    val meals: List<String?>,
    val specialMenu: List<String>?,
    val optionalIngredients: List<String?>?,
)

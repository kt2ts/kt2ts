package cookbook

enum class MealType {
    Breakfast,
    Lunch,
    Dinner,
    Snack,
    Dessert,
}

data class MenuItem(
    val name: String,
    val mealType: MealType,
    val price: Double,
)

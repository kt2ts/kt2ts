package cookbook

sealed class DietaryRestriction

data class Allergy(
    val allergen: String,
    val severity: String,
) : DietaryRestriction()

data object Vegan : DietaryRestriction()

data object GlutenFree : DietaryRestriction()

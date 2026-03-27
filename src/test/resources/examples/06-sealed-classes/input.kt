package cookbook

sealed class CookingMethod

data class Baking(
    val temperatureCelsius: Int,
    val durationMinutes: Int,
    val fanAssisted: Boolean,
) : CookingMethod()

data class Frying(
    val oilType: String,
    val temperatureCelsius: Int,
) : CookingMethod()

data class Steaming(
    val durationMinutes: Int,
    val pressureCooker: Boolean,
) : CookingMethod()

data class RawPreparation(
    val marinadeMinutes: Int?,
) : CookingMethod()

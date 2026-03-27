package cookbook

data class Kitchen(
    val name: String,
    val equipment: List<Kitchen.Appliance>,
) {
    data class Appliance(
        val name: String,
        val wattage: Int,
    )
}

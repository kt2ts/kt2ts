package cookbook

interface Named {
    val name: String
}

data class Chef(
    override val name: String,
    val specialty: String,
) : Named

data class Restaurant(
    override val name: String,
    val michelinStars: Int,
) : Named

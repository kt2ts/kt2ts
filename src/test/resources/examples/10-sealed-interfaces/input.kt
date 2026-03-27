package cookbook

sealed interface Measurement {
    data class Weight(val grams: Double) : Measurement
    data class Volume(val milliliters: Double) : Measurement
    data class Pieces(val count: Int) : Measurement
}

package api.models

import kotlinx.serialization.Serializable

@Serializable
data class IngredientUnits(
    val ingredient: Ingredients,
    val amount: Double,
    val unitType: String
) {

}
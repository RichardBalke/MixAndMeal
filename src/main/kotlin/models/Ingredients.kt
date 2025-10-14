package api.models

import kotlinx.serialization.Serializable

@Serializable
data class Ingredients(
    val name: String,
    val description: String,
    val allergens: List<Allergens> = listOf<Allergens>(),

    val id: Long = NEW_INGREDIENT_ID
) {
    companion object {
        const val NEW_INGREDIENT_ID: Long = 0
    }
}
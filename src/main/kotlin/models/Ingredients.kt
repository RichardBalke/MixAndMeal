package api.models

data class Ingredients(
    val id: Long = NEW_INGREDIENT_ID,
    val name: String,
    val description: String,
    val allergens: List<Allergens>
) {
    companion object {
        const val NEW_INGREDIENT_ID: Long = 0
    }
}
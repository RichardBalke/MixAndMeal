package api.models

import kotlinx.serialization.Serializable

@Serializable
data class Recipes (
    val title: String,
    val description: String,
    val prepTime: Int,
    val cookingTime: Int,
    val difficulty: Difficulty,
    val image: String,
    val mealType: MealType?,
    val kitchenStyle: KitchenStyle?,
    val diets: List<Diets> = listOf<Diets>(),
    val ingredients: List<MutableMap<Ingredients, Pair<UnitType, Double>>>,

    val id: Long = NEW_RECIPE_ID
    ) {
    companion object {
        const val NEW_RECIPE_ID: Long = 0
    }
}


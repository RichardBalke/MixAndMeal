package api.models

data class Recipes (
    val id: Long = NEW_RECIPE_ID,
    val title: String,
    val description: String,
    val prepTime: Int,
    val cookingTime: Int,
    val difficulty: Difficulty,
    val image: String,
    val mealType: MealType?,
    val kitchenStyle: KitchenStyle,
    val diets: List<Diets>,
    val ingredients: List<MutableMap<Ingredients, Pair<UnitType, Double>>>
    ) {

    companion object {
        const val NEW_RECIPE_ID: Long = 0
    }
}

enum class Diets(val id: Int, val displayName: String, val description: String) {
    MILK(1, "Melk", "Lactose intolerantie"),
    EGGS(2, "Eieren", "Ei Allergie"),
    FISH(3, "Vis", "Vis Allergie"),
    CRUSTACEANSHELLFISH(4, "Kreeften, Garnalen", "Schaaldier Allergie"),
    TREENUTS(5, "Noten", "Pinda Allergie"),
    SOYBEANS(7, "Soya", "Soya Allergie"),
    SESAME(8, "Sesam", "Sesam Allergie"),
    LUPIN(9, "Lupin", "Lupine Allergie"),
    MOLLUSKS(10, "Mosselen, Oesters", "Weekdier Allergie"),
    GLUTEN(11, "Gluten", "Gluten Allergie"),
    MUSTARD(12, "Mosterd", "Mosterd Allergie"),
}

enum class Difficulty {
    Easy,
    Medium,
    Hard
}

enum class MealType {
    Breakfast,
    Lunch,
    Dinner
}

enum class KitchenStyle {
    Asian,
    EastEuropean,
    Mexican,
    Dutch,
    Turkish,
    Greek
}
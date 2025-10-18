package api.models

import kotlinx.serialization.Serializable

@Serializable
enum class MealType(mealTypeName: String) {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner"),
    DESSERT("dessert")
}
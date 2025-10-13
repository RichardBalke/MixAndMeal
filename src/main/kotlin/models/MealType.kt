package api.models

import kotlinx.serialization.Serializable

@Serializable
enum class MealType {
    Breakfast,
    Lunch,
    Dinner,
    Dessert
}
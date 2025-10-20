package api.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val password: String,
    val email: String,
//    val locationSettings: LocationSettings(), wordt toegevoegd bij implementatie app
    val role: Role = Role.USER,
    val favourites: List<Recipes> = listOf<Recipes>(),
//    val fridge: List<Ingredients> = emptyList(), wordt toegevoegd bij implementatie app
    val allergens: List<Allergens> = listOf<Allergens>(),
    val id: Long = NEW_USER_ID
) {
    companion object {
        const val NEW_USER_ID: Long = 0L
    }
}


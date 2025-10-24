package api.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val password: String,
    val email: String,
//    val locationSettings: LocationSettings(), wordt toegevoegd bij implementatie app
    val role: Role = Role.USER,
    val favourites: MutableList<Recipes> = mutableListOf<Recipes>(),
    val fridge: MutableList<Ingredients> = mutableListOf<Ingredients>(),
    val allergens: MutableList<Allergens> = mutableListOf<Allergens>(),
    val id: Long = NEW_USER_ID
) {
    companion object {
        const val NEW_USER_ID: Long = 0L
    }
}


package api.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val password: String,
    val email: String,
//    val locationSettings: LocationSettings(),
//    val list: List<Ingredients>,
    val role: Role = Role.USER,
//    val favourites: List<Recipes>,
//    val fridge: List<Ingredients>,
    val id: Int = NEW_USER_ID
) {
    companion object {
        const val NEW_USER_ID: Int = 0
    }
}


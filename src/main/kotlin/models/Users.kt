package api.models

data class User(
    val id: Long = NEW_USER_ID,
    val name: String,
    val password: String,
    val email: String,
//    val locationSettings: LocationSettings(),
//    val list: MutableListOf()<Ingredients>
    val role: Role = Role.USER,
    val favourites: List<Recipes>,
    val fridge: List<Ingredients>
) {
    companion object {
        const val NEW_USER_ID: Long = 0
    }
}


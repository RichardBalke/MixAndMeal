package api.requests

import api.models.Role

data class User(
    val name: String,
    val email: String,
//    val locationSettings: LocationSettings(),
//    val list: MutableListOf()<Ingredients>
    val role: Role = Role.USER
) {

}

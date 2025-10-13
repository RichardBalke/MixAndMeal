package api.requests

import api.models.Recipes
import kotlinx.serialization.Serializable

@Serializable
data class ReadUserRequest(
    val name: String,
    val email: String,
    //    val locationSettings: LocationSettings(),
) {

}
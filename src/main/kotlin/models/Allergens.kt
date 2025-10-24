package api.models

import kotlinx.serialization.Serializable

@Serializable
data class Allergens(
    val id: Int,
    val name: String,
    val displayName: String,
    val description: String
)


package api.models

import kotlinx.serialization.Serializable

@Serializable
enum class KitchenStyle {
    Asian,
    EastEuropean,
    Mexican,
    Dutch,
    Turkish,
    Greek
}
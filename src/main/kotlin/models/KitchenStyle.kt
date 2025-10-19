package api.models

import kotlinx.serialization.Serializable

@Serializable
enum class KitchenStyle(kitchenName: String?) {
    ASIAN("asian"),
    EAST_EUROPEAN("easteuropean"),
    MEXICAN("mexican"),
    DUTCH("dutch"),
    TURKISH("turkish"),
    GREEK("greek"),
    MEDITERRANEAN("mediterranean"),
    JAPANESE("japanese"),
    INDIAN("indian"),
    CHINESE("chinese"),
    ITALIAN("italian"),
    FRENCH("french"),
    THAI("thai"),
    SPANISH("spanish"),
    KOREAN("korean"),
    VIETNAMESE("vietnamese")
}

package api.models

import kotlinx.serialization.Serializable

@Serializable
enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
}
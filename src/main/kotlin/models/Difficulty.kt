package api.models

import kotlinx.serialization.Serializable

@Serializable
enum class Difficulty(difficultyName: String) {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard"),
}
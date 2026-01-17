package service

import models.dto.AllergenEntry
import repository.AllergensRepositoryImpl

class AllergenService(private val allergenRepository : AllergensRepositoryImpl) {
    suspend fun getAllergenById(id: Int): AllergenEntry? {
        return allergenRepository.findById(id)
    }

    suspend fun getAllergenByDisplayName(displayName: String): AllergenEntry? {
        return allergenRepository.findByDisplayName(displayName)
    }
}
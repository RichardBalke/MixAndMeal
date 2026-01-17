package service

import models.dto.DietEntry
import repository.DietsRepositoryImpl

class DietsService(private val dietsRepository: DietsRepositoryImpl) {
    suspend fun getDietById(id: Int) : DietEntry? {
        return dietsRepository.findByDietId(id)
    }
}
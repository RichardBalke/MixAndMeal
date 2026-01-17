package api.service

import api.repository.IngredientsRepositoryImpl
import models.dto.IngredientEntry


class IngredientService(private val ingredientsRepository: IngredientsRepositoryImpl) {
    suspend fun getIngredientsByName(name: String): IngredientEntry? {
        return ingredientsRepository.findByName(name)
    }


}
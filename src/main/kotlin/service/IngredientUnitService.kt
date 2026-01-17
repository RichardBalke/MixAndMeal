package service

import api.repository.IngredientUnitRepositoryImpl
import models.dto.IngredientUnitEntry

class IngredientUnitService(private val ingredientUnitRepository: IngredientUnitRepositoryImpl) {
    suspend fun getIngredientsByRecipeId(id : Int): List<IngredientUnitEntry>{
        return ingredientUnitRepository.findAllByRecipeId(id)
    }

    suspend fun addIngredientUnit(
        ingredientUnit: IngredientUnitEntry
    ){
        ingredientUnitRepository.create(ingredientUnit)
    }

    suspend fun deleteAllRecipeIngredients(recipeId: Int) : Boolean {
        ingredientUnitRepository.deleteIngredientsByRecipeId(recipeId)
        val exists = ingredientUnitRepository.findAllByRecipeId(recipeId)
        return exists.isEmpty()
    }
}
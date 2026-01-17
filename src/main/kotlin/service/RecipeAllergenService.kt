package service

import models.dto.AllergenEntry
import models.dto.RecipeAllergenEntry
import models.dto.RecipeEntry
import repository.RecipeAllergensRepositoryImpl

class RecipeAllergenService(val recipeAllergensRepository: RecipeAllergensRepositoryImpl) {
    suspend fun getAllergensByRecipeId(recipeId : Int, allergenService : AllergenService): List<AllergenEntry> {
        val recipeAllergens =  recipeAllergensRepository.findAllByRecipeId(recipeId)
        val allergens = mutableListOf<AllergenEntry>()
        for (allergenEntry in recipeAllergens) {
            val allergen = allergenService.getAllergenById(allergenEntry.allergenId)
            if (allergen != null) {
                allergens.add(allergen)
            }
        }
        return allergens
    }

    suspend fun getRecipesByAllergenId(allergenId : Int, recipeService : RecipeService): List<RecipeEntry> {
        val recipeAllergenList = recipeAllergensRepository.findAllByAllergenId(allergenId)
        val recipes = mutableListOf<RecipeEntry>()
        for(recipeEntry in recipeAllergenList) {
            val recipe = recipeService.getRecipe(recipeEntry.recipeId)
            if(recipe != null) {
                recipes.add(recipe)
            }
        }
        return recipes
    }

    suspend fun addRecipeAllergen(
        recipeAllergen: RecipeAllergenEntry
    ){
        recipeAllergensRepository.create(recipeAllergen)
    }

    suspend fun deleteAllRecipeAllergens(recipeId: Int) : Boolean {
        recipeAllergensRepository.deleteDietsByRecipeId(recipeId)
        val exists = recipeAllergensRepository.findAllByRecipeId(recipeId)
        return exists.isEmpty()
    }
}
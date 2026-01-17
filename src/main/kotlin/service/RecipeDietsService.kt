package service


import models.dto.DietEntry
import models.dto.RecipeDietEntry
import models.tables.RecipeDiets
import repository.RecipeDietsRepositoryImpl

class RecipeDietsService(private val recipeDietsRepository: RecipeDietsRepositoryImpl) {
    suspend fun getDietsbyRecipeId(recipeID: Int, dietsService: DietsService): List<DietEntry> {
        val recipeDiets =  recipeDietsRepository.findAllByRecipeId(recipeID)
        val diets = mutableListOf<DietEntry>()
        for (dietEntry in recipeDiets) {
            val diet = dietsService.getDietById(dietEntry.dietId)
            if (diet != null) {
                diets.add(diet)
            }
        }
        return diets
    }
    suspend fun addRecipeDiet(
        recipeDiets: RecipeDietEntry
    ){
        recipeDietsRepository.create(recipeDiets)
    }

    suspend fun deleteAllRecipeDiets(recipeId: Int) : Boolean {
        recipeDietsRepository.deleteDietsByRecipeId(recipeId)
        val exists = recipeDietsRepository.getDietsForRecipe(recipeId)
        return exists.isEmpty()
    }
}
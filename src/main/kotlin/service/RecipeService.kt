package service

import api.repository.RecipesRepository
import api.repository.RecipesRepositoryImpl
import api.requests.RecipeUploadRequest
import api.responses.RecipeCardResponse
import models.dto.IngredientUnitEntry
import models.dto.RecipeAllergenEntry
import models.dto.RecipeDietEntry
import models.dto.RecipeEntry
import models.dto.UserFavouritesEntry
import models.tables.Recipes
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.java.KoinJavaComponent.inject
import repository.AllergensRepository
import repository.DietsRepository
import repository.RecipeAllergensRepositoryImpl
import requests.RecipeSearchRequest
import kotlin.String

class RecipeService(
    private val recipeRepository : RecipesRepository,
    private val dietsRepository: DietsRepository,
    private val allergensRepository: AllergensRepository
) {

    suspend fun createUploadedRecipe(
        uploadedRecipe: RecipeUploadRequest,
        recipeDietsService: RecipeDietsService,
        recipeAllergenService: RecipeAllergenService,
        ingredientUnitService: IngredientUnitService
    ): Int {
        return newSuspendedTransaction {
            val recipe = RecipeEntry(
                null,
                uploadedRecipe.title,
                uploadedRecipe.description,
                uploadedRecipe.instructions,
                uploadedRecipe.prepTime,
                uploadedRecipe.cookingTime,
                uploadedRecipe.difficulty,
                uploadedRecipe.mealType,
                uploadedRecipe.kitchenStyle,
                0
            )
            val newRecipe = recipeRepository.create(recipe)  // Changed from addRecipes

            uploadedRecipe.diets.forEach { dietRequest ->
                dietsRepository.findByDisplayName(dietRequest.displayName)?.let { diet ->
                    recipeDietsService.addRecipeDiet(RecipeDietEntry(newRecipe.id?.toInt() ?: 0, diet.id))
                }
            }

            uploadedRecipe.allergens.forEach { allergenRequest ->
                allergensRepository.findByDisplayName(allergenRequest.displayName)?.let { allergen ->
                    recipeAllergenService.addRecipeAllergen(RecipeAllergenEntry(newRecipe.id?.toInt() ?: 0, allergen.id))
                }
            }

            uploadedRecipe.ingredients.forEach { ingredient ->
                ingredientUnitService.addIngredientUnit(
                    IngredientUnitEntry(newRecipe.id?.toInt() ?: 0, ingredient.ingredientName, ingredient.amount, ingredient.unitType)
                )
            }
            newRecipe.id?.toInt() ?: 0
        }
    }


    fun formatCookingTime(minutes: Int): String {
        val hours = minutes / 60
        val minutes = minutes % 60
        return "${hours}h ${minutes}m"
    }

    suspend fun searchRecipes(recipeSearchRequest: RecipeSearchRequest) : List<RecipeCardResponse> {
        return recipeRepository.searchRecipes(recipeSearchRequest).toList()
    }

    suspend fun getAllRecipes(): List<RecipeCardResponse> {
        return recipeRepository.findAllRecipesAsRecipeCards()
    }

    suspend fun addRecipes(recipe: RecipeEntry): RecipeEntry {
        return recipeRepository.create(recipe)
    }

    suspend fun getRecipe(id: Int): RecipeEntry? {
        return recipeRepository.findByRecipeId(id)
    }

    suspend fun deleteRecipe(id: Int): Boolean {
        return recipeRepository.delete(id)
    }

    suspend fun findByTitle(title: String): List<RecipeEntry> {
        return recipeRepository.findByTitle(title)
    }

    suspend fun findByDifficulty(difficulty: String): List<RecipeEntry> {
        return recipeRepository.findByDifficulty(difficulty)
    }

    suspend fun findByMealType(mealType: String): List<RecipeEntry> {
        return recipeRepository.findByMealType(mealType)
    }

    suspend fun findByDiets(diets: String): List<RecipeEntry> {
        return recipeRepository.findByDiets(diets)
    }

    suspend fun findByKitchenStyle(kitchenStyle: String): List<RecipeEntry> {
        return recipeRepository.findByKitchenStyle(kitchenStyle)
    }

    suspend fun findPopularRecipes(limit: Int, recipeImagesService: RecipeImagesService): List<RecipeCardResponse> {
        val recipes = recipeRepository.findPopularRecipes(limit)
        for (recipe in recipes) {
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }

    suspend fun findRecipesByDifficulty(
        limit: Int,
        difficulty: String,
        recipeImagesService: RecipeImagesService
    ): List<RecipeCardResponse> {
        val recipes = recipeRepository.findRecipeCardsByDifficulty(limit, difficulty)
        for (recipe in recipes) {
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }

    suspend fun findQuickRecipes(limit: Int, recipeImagesService: RecipeImagesService): List<RecipeCardResponse> {
        val recipes = recipeRepository.findQuickRecipes(limit)
        for (recipe in recipes) {
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }

    suspend fun findFavouriteRecipes(
        recipeIds: List<UserFavouritesEntry>,
        recipeImagesService: RecipeImagesService
    ): List<RecipeCardResponse> {
        val recipes = recipeRepository.findFavoriteRecipes(recipeIds)
        for (recipe in recipes) {
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }
}

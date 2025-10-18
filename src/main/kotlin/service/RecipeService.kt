package service

import api.models.Diets
import api.models.Difficulty
import api.models.KitchenStyle
import api.repository.RecipesRepository
import api.repository.FakeRecipeRepository.currentID
import api.repository.FakeRecipeRepository.recipes
import api.models.Recipes


class RecipeService : RecipesRepository {

    override suspend fun findAll(): List<Recipes> {
        return recipes.toList()
    }

    override suspend fun formatCookingTime(minutes: Int): String {
        val hours = minutes / 60
        val minutes = minutes % 60
        return "${hours}h ${minutes}m"
    }

    override suspend fun create(entity: Recipes): Recipes {
        currentID++
        val newRecipe = entity.copy(id = currentID)
        recipes.add(newRecipe)
        return newRecipe
    }

    override suspend fun findByTitle(title: String): List<Recipes> {

        val recipeTitles = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.title.lowercase().contains(title.lowercase())) {
                recipeTitles.add(recipe)
            }
        }
        return recipeTitles
    }

    override suspend fun findByMealType(mealType: String?): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.mealType?.name?.lowercase() == mealType) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes
    }

    override suspend fun findByDifficulty(difficulty: Difficulty): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.difficulty == difficulty) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes
    }

    override suspend fun findByDiets(diets: Diets): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (diets in recipe.diets) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes
    }

    override suspend fun findByKitchenStyle(kitchenStyle: KitchenStyle): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.kitchenStyle == kitchenStyle) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes
    }



    override suspend fun findById(id: Long): Recipes? {
        return recipes.find { it.id == id }
    }

    override suspend fun delete(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun update(entity: Recipes) {
        TODO("Not yet implemented")
    }
}
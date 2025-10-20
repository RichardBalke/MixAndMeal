package service

import api.models.Diets
import api.models.KitchenStyle
import api.repository.RecipesRepository
import api.repository.FakeRecipeRepository.currentID
import api.repository.FakeRecipeRepository.recipes
import api.models.Recipes
import api.repository.FakeUserRepository.users


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

    override suspend fun findByDifficulty(difficulty: String?): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.difficulty.name.lowercase() == difficulty) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes
    }

    override suspend fun findByDiets(diets: String?): List<Recipes> {

        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            val matchedDiets = recipe.diets.any { it.displayName.lowercase() == diets?.lowercase() }
            if (matchedDiets) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes

    }


    override suspend fun findByKitchenStyle(kitchenStyle: String?): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.kitchenStyle?.name?.lowercase() == kitchenStyle) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes
    }


    override suspend fun findById(id: Long): Recipes? {
        return recipes.find { it.id == id }
    }

    override suspend fun delete(id: Long): Boolean {
        return recipes.removeIf { id == it.id}
    }

    override suspend fun update(entity: Recipes) {
        TODO("Not yet implemented")
    }

//    Ingredienten en recepten matching functie. Matcht op minsten 1 ingredient aanwezig. sortering op beste matches
//
//    suspend fun findBestMatchesByIngredients(selectedIngredients: List<String>): List<Pair<Recipe, Double>> {
//        return recipes
//            .map { recipe ->
//                val totalIngredients = recipe.ingredients.size
//                val matchedCount = recipe.ingredients.count { selectedIngredients.contains(it.ingredient.name) }
//                val matchPercentage = if (totalIngredients > 0) matchedCount.toDouble() / totalIngredients else 0.0
//                recipe to matchPercentage
//            }
//            .filter { it.second > 0 }
//            .sortedByDescending { it.second }
//    }
}

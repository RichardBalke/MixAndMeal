package api.repository

import api.models.Diets
import api.models.Difficulty
import api.models.Ingredients
import api.models.KitchenStyle
import api.models.MealType
import api.models.Recipes
import api.models.UnitType
import kotlinx.coroutines.runBlocking

interface RecipesRepository : CrudRepository<Recipes, Long> {
    suspend fun findByTitle(title: String): List<Recipes>
    suspend fun findByDifficulty(difficulty: Difficulty): List<Recipes>
    suspend fun findByMealType(mealType: MealType): List<Recipes>
    suspend fun findByDiets(diets: Diets): List<Recipes>
    suspend fun findByKitchenStyle(kitchenStyle: KitchenStyle): List<Recipes>
    suspend fun formatCookingTime(minutes: Int): String

//    suspend fun updateImage(recipeID: Long, imageUrl: String): Boolean
//    suspend fun findByFavourites(favourites : Favourites): List<Recipes>
}

object FakeRecipeRepository : RecipesRepository {
    private var currentID: Long = 0
    private val recipes = mutableListOf<Recipes>()

    init {
        runBlocking {
            create(
                Recipes(
                    title = "Creamy Mushroom Risotto",
                    description = "A rich and creamy risotto with earthy mushrooms and parmesan.",
                    prepTime = 15,
                    cookingTime = 300,
                    difficulty = Difficulty.MEDIUM,
                    image = "https://example.com/images/risotto.jpg",
                    mealType = MealType.DINNER,
                    kitchenStyle = KitchenStyle.ITALIAN,
                    diets = listOf(Diets.VEGETARIAN),
                    ingredients = listOf(
                        resolveIngredients(
                            "Apple" to (UnitType.KG to 1.0),
                            "Sugar" to (UnitType.GRAMS to 50.0),
                            "Peanut" to (UnitType.GRAMS to 20.0)
                        )
                    )
                )
            )
        }
    }




    private suspend fun resolveIngredients(vararg entries: Pair<String, Pair<UnitType, Double>>): MutableMap<Ingredients, Pair<UnitType, Double>> {
        val resolved = mutableMapOf<Ingredients, Pair<UnitType, Double>>()
        for ((name, quantity) in entries) {
            val ingredient = FakeIngredientsRepository.findByName(name)
            if (ingredient != null) {
                resolved[ingredient] = quantity
            } else {
                println("⚠️ Ingredient '$name' not found in repository.")
            }
        }
        return resolved
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
            if (recipe.title.contains("$title")) {
                recipeTitles.add(recipe)
            }
        }
        return recipeTitles
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

    override suspend fun findByMealType(mealType: MealType): List<Recipes> {
        val foundRecipes = mutableListOf<Recipes>()
        for (recipe in recipes) {
            if (recipe.mealType == mealType) {
                foundRecipes.add(recipe)
            }
        }
        return foundRecipes
    }

    override suspend fun findAll(): List<Recipes> {
        return recipes.toList()
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
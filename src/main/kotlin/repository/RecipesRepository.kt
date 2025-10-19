package api.repository

import api.models.Diets
import api.models.Difficulty
import api.models.IngredientUnits
import api.models.KitchenStyle
import api.models.MealType
import api.models.Recipes
import kotlinx.coroutines.runBlocking
import service.RecipeService
import api.repository.FakeIngredientUnitRepository.list

interface RecipesRepository : CrudRepository<Recipes, Long> {
    suspend fun findByTitle(title: String): List<Recipes>
    suspend fun findByDifficulty(difficulty: String?): List<Recipes>
    suspend fun findByMealType(mealType: String?): List<Recipes>
    suspend fun findByDiets(diets: String?): List<Recipes>
    suspend fun findByKitchenStyle(kitchenStyle: String?): List<Recipes>
    suspend fun formatCookingTime(minutes: Int): String

//    suspend fun updateImage(recipeID: Long, imageUrl: String): Boolean
//    suspend fun findByFavourites(favourites : Favourites): List<Recipes>
}

object FakeRecipeRepository {
    var currentID: Long = 0
    val recipes = mutableListOf<Recipes>()
    val recipeService = RecipeService()

    init {
        runBlocking {

           recipeService.create(
                Recipes(
                    title = "Creamy Mushroom Risotto",
                    description = "A rich and creamy risotto with earthy mushrooms and parmesan.",
                    prepTime = 15,
                    cookingTime = 300,
                    difficulty = Difficulty.MEDIUM,
                    image = "https://example.com/images/risotto.jpg",
                    mealType = MealType.DINNER,
                    kitchenStyle = KitchenStyle.ITALIAN,
                    diets = listOf(Diets.VEGAN),
                    ingredients = listOf<IngredientUnits>(
                        list[1],
                        list[2]
                    )
                )
            )

            recipeService.create(
                Recipes(
                    title = "Creamy Mushroom Risotto",
                    description = "A rich and creamy risotto with earthy mushrooms and parmesan.",
                    prepTime = 15,
                    cookingTime = 300,
                    difficulty = Difficulty.MEDIUM,
                    image = "https://example.com/images/risotto.jpg",
                    mealType = MealType.DINNER,
                    kitchenStyle = KitchenStyle.ITALIAN,
                    diets = listOf(Diets.VEGAN),
                    ingredients = listOf<IngredientUnits>(
                        list[1],
                        list[2]
                    )
                )
            )

            recipeService.create(
                Recipes(
                    title = "Creamy Mushroom Risotto",
                    description = "A rich and creamy risotto with earthy mushrooms and parmesan.",
                    prepTime = 15,
                    cookingTime = 300,
                    difficulty = Difficulty.MEDIUM,
                    image = "https://example.com/images/risotto.jpg",
                    mealType = MealType.DINNER,
                    kitchenStyle = KitchenStyle.ITALIAN,
                    diets = listOf(Diets.VEGAN),
                    ingredients = listOf<IngredientUnits>(
                        list[1],
                        list[2]
                    )
                )
            )


        }
    }

}
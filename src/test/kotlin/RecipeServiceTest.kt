//package service
//
//import api.models.Diets
//import models.enums.Difficulty
//import api.models.IngredientUnits
//import models.enums.KitchenStyle
//import models.enums.MealType
//import api.models.Recipes
//import api.repository.FakeIngredientUnitRepository
//import api.repository.FakeRecipeRepository
//import kotlinx.coroutines.runBlocking
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class RecipeServiceTest {
//
//    @Test
//    fun `formatCookingTime formats minutes to hours and minutes`() = runBlocking {
//        val service = RecipeService()
//        val formatted = service.formatCookingTime(125)
//        assertEquals("2h 5m", formatted)
//    }
//
//}

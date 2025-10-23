import service.RecipeService
import api.models.Diets
import api.models.Difficulty
import api.models.KitchenStyle
import api.models.MealType
import api.models.Recipes
import api.repository.FakeRecipeRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class RecipeServiceTest {

    private val service = RecipeService()

    @BeforeEach
    fun setup() {
        // reset in-memory repository between tests
        FakeRecipeRepository.recipes.clear()
        FakeRecipeRepository.currentID = 0
    }

    @Test
    fun `formatCookingTime returns hours and minutes`() {
        val result = runBlocking { service.formatCookingTime(125) }
        assertEquals("2h 5m", result)
    }

    @Test
    fun `create assigns id and can be retrieved by id`() = runBlocking {
        val recipe = Recipes(
            title = "Test Soup",
            description = "desc",
            prepTime = 10,
            cookingTime = 30,
            difficulty = Difficulty.EASY,
            image = "img.jpg",
            mealType = MealType.DINNER,
            kitchenStyle = KitchenStyle.INDIAN,
            diets = listOf(Diets.VEGAN),
            ingredients = emptyList()
        )

        val created = service.create(recipe)
        assertTrue(created.id > 0)

        val fetched = service.findById(created.id)
        assertNotNull(fetched)
        assertEquals("Test Soup", fetched?.title)
    }

    @Test
    fun `findAll returns all created recipes`() = runBlocking {
        val r1 = service.create(
            Recipes(
                title = "A",
                description = "d",
                prepTime = 1,
                cookingTime = 1,
                difficulty = Difficulty.EASY,
                image = "",
                mealType = MealType.BREAKFAST,
                kitchenStyle = KitchenStyle.ITALIAN,
                diets = listOf(Diets.VEGAN),
                ingredients = emptyList()
            )
        )
        val r2 = service.create(
            Recipes(
                title = "B",
                description = "d",
                prepTime = 1,
                cookingTime = 1,
                difficulty = Difficulty.MEDIUM,
                image = "",
                mealType = MealType.DINNER,
                kitchenStyle = KitchenStyle.FRENCH,
                diets = listOf(Diets.VEGAN),
                ingredients = emptyList()
            )
        )

        val all = service.findAll()
        assertEquals(2, all.size)
        assertTrue(all.any { it.id == r1.id })
        assertTrue(all.any { it.id == r2.id })
    }

    @Test
    fun `update modifies an existing recipe`() = runBlocking {
        val created = service.create(
            Recipes(
                title = "Orig",
                description = "d",
                prepTime = 1,
                cookingTime = 1,
                difficulty = Difficulty.EASY,
                image = "",
                mealType = MealType.DINNER,
                kitchenStyle = KitchenStyle.INDIAN,
                diets = listOf(Diets.VEGAN),
                ingredients = emptyList()
            )
        )

        val updated = created.copy(title = "Updated Title")
        service.update(updated)

        val fetched = service.findById(created.id)
        assertNotNull(fetched)
        assertEquals("Updated Title", fetched?.title)
    }

    @Test
    fun `delete removes a recipe`() = runBlocking {
        val created = service.create(
            Recipes(
                title = "ToDelete",
                description = "d",
                prepTime = 1,
                cookingTime = 1,
                difficulty = Difficulty.EASY,
                image = "",
                mealType = MealType.DINNER,
                kitchenStyle = KitchenStyle.INDIAN,
                diets = listOf(Diets.VEGAN),
                ingredients = emptyList()
            )
        )

        val deleted = service.delete(created.id)
        assertTrue(deleted)
        assertNull(service.findById(created.id))
    }

    @Test
    fun `findByTitle returns matching recipes`() = runBlocking {
        service.create(
            Recipes(
                title = "Spicy Lentil Soup",
                description = "d",
                prepTime = 1,
                cookingTime = 1,
                difficulty = Difficulty.EASY,
                image = "",
                mealType = MealType.DINNER,
                kitchenStyle = KitchenStyle.INDIAN,
                diets = listOf(Diets.VEGAN),
                ingredients = emptyList()
            )
        )

        val results = service.findByTitle("lentil")
        assertTrue(results.isNotEmpty())
        assertTrue(results.any { it.title.contains("Lentil", ignoreCase = true) })
    }

    @Test
    fun `findByMealType difficulty diets and kitchen style filters work`() = runBlocking {
        val r = service.create(
            Recipes(
                title = "FilterTest",
                description = "d",
                prepTime = 1,
                cookingTime = 1,
                difficulty = Difficulty.MEDIUM,
                image = "",
                mealType = MealType.DINNER,
                kitchenStyle = KitchenStyle.ITALIAN,
                diets = listOf(Diets.VEGAN),
                ingredients = emptyList()
            )
        )

        val byMeal = service.findByMealType("dinner")
        assertTrue(byMeal.any { it.id == r.id })

        val byDiff = service.findByDifficulty("medium")
        assertTrue(byDiff.any { it.id == r.id })

        val byDiet = service.findByDiets("vegan")
        assertTrue(byDiet.any { it.id == r.id })

        val byKitchen = service.findByKitchenStyle("italian")
        assertTrue(byKitchen.any { it.id == r.id })
    }

    @Test
    fun `update non existent recipe throws`() = runBlocking {
        val nonExistent = Recipes(
            id = 999,
            title = "Nope",
            description = "d",
            prepTime = 1,
            cookingTime = 1,
            difficulty = Difficulty.EASY,
            image = "",
            mealType = MealType.DINNER,
            kitchenStyle = KitchenStyle.INDIAN,
            diets = listOf(Diets.VEGAN),
            ingredients = emptyList()
        )

        val ex = assertThrows(IllegalArgumentException::class.java) {
            runBlocking { service.update(nonExistent) }
        }
        assertTrue(ex.message?.contains("not found") == true)
    }
}

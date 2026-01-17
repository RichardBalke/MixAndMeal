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
//class RecipeRepositoryTest {
//
//    @BeforeEach
//    fun setup() {
//        // Reset fake repository state to avoid cross-test contamination
//        FakeRecipeRepository.recipes.clear()
//        FakeRecipeRepository.currentID = 0
//
//        // Ensure ingredient units are initialized (from FakeIngredientUnitRepository)
//        // Access the list to trigger init block if needed
//        assertTrue(FakeIngredientUnitRepository.list.isNotEmpty())
//    }
//
//    private fun sampleRecipe(
//        title: String = "Test Recipe",
//        difficulty: Difficulty = Difficulty.EASY,
//        mealType: MealType? = MealType.DINNER,
//        kitchen: KitchenStyle? = KitchenStyle.ITALIAN,
//        diets: List<Diets> = listOf(Diets.VEGAN),
//        prep: Int = 10,
//        cook: Int = 25,
//        ingredients: List<IngredientUnits> = listOf(
//            FakeIngredientUnitRepository.list[0],
//            FakeIngredientUnitRepository.list[1]
//        )
//    ): Recipes = Recipes(
//        title = title,
//        description = "Desc",
//        prepTime = prep,
//        cookingTime = cook,
//        difficulty = difficulty,
//        image = "http://img",
//        mealType = mealType,
//        kitchenStyle = kitchen,
//        diets = diets,
//        ingredients = ingredients
//    )
//
//    @Test
//    fun `create assigns id and stores recipe`() = runBlocking {
//        val created = FakeRecipeRepository.create(sampleRecipe(title = "First"))
//
//        assertEquals(1, created.id)
//        assertEquals("First", created.title)
//        assertEquals(1, FakeRecipeRepository.recipes.size)
//    }
//
//    @Test
//    fun `findAll returns all recipes`() = runBlocking {
//        FakeRecipeRepository.create(sampleRecipe(title = "R1"))
//        FakeRecipeRepository.create(sampleRecipe(title = "R2"))
//
//        val all = FakeRecipeRepository.findAll()
//
//        assertEquals(2, all.size)
//        assertTrue(all.any { it.title == "R1" })
//        assertTrue(all.any { it.title == "R2" })
//    }
//
//    @Test
//    fun `findByTitle returns case-insensitive partial matches`() = runBlocking {
//        FakeRecipeRepository.create(sampleRecipe(title = "Creamy Mushroom Risotto"))
//        FakeRecipeRepository.create(sampleRecipe(title = "Spicy Mushroom Soup"))
//        FakeRecipeRepository.create(sampleRecipe(title = "Apple Pie"))
//
//        val found = FakeRecipeRepository.findByTitle("mush")
//
//        assertEquals(2, found.size)
//        assertTrue(found.all { it.title.lowercase().contains("mush") })
//    }
//
//    @Test
//    fun `findByMealType matches by lowercase name`() = runBlocking {
//        FakeRecipeRepository.create(sampleRecipe(title = "Dinner One", mealType = MealType.DINNER))
//        FakeRecipeRepository.create(sampleRecipe(title = "Lunch One", mealType = MealType.LUNCH))
//
//        val dinners = FakeRecipeRepository.findByMealType("dinner")
//
//        assertEquals(1, dinners.size)
//        assertEquals("Dinner One", dinners.first().title)
//    }
//
//    @Test
//    fun `findByDifficulty matches by lowercase enum name`() = runBlocking {
//        FakeRecipeRepository.create(sampleRecipe(title = "Easy R", difficulty = Difficulty.EASY))
//        FakeRecipeRepository.create(sampleRecipe(title = "Hard R", difficulty = Difficulty.HARD))
//
//        val easy = FakeRecipeRepository.findByDifficulty("easy")
//
//        assertEquals(1, easy.size)
//        assertEquals("Easy R", easy.first().title)
//    }
//
//    @Test
//    fun `findByDiets matches by display name`() = runBlocking {
//        FakeRecipeRepository.create(sampleRecipe(title = "Vegan R", diets = listOf(Diets.VEGAN)))
//        FakeRecipeRepository.create(sampleRecipe(title = "Vegetarian R", diets = listOf(Diets.VEGETARIAN)))
//
//        val vegan = FakeRecipeRepository.findByDiets(Diets.VEGAN.displayName) // uses displayName matching
//
//        assertEquals(1, vegan.size)
//        assertEquals("Vegan R", vegan.first().title)
//    }
//
//    @Test
//    fun `findByKitchenStyle matches by lowercase enum name`() = runBlocking {
//        FakeRecipeRepository.create(sampleRecipe(title = "Italian R", kitchen = KitchenStyle.ITALIAN))
//        FakeRecipeRepository.create(sampleRecipe(title = "Mexican R", kitchen = KitchenStyle.MEXICAN))
//
//        val italian = FakeRecipeRepository.findByKitchenStyle("italian")
//
//        assertEquals(1, italian.size)
//        assertEquals("Italian R", italian.first().title)
//    }
//
//    @Test
//    fun `findById returns correct recipe`() = runBlocking {
//        val r = FakeRecipeRepository.create(sampleRecipe(title = "Find Me"))
//
//        val found = FakeRecipeRepository.findById(r.id)
//
//        assertEquals(r, found)
//    }
//
//    @Test
//    fun `update replaces existing recipe`() = runBlocking {
//        val created = FakeRecipeRepository.create(sampleRecipe(title = "Original"))
//        val modified = created.copy(title = "Updated")
//
//        FakeRecipeRepository.update(modified)
//
//        val found = FakeRecipeRepository.findById(created.id)
//        assertEquals("Updated", found?.title)
//    }
//
//    @Test
//    fun `update throws when ID is zero`() {
//        val r = sampleRecipe(title = "Invalid") // id == 0 by default
//
//        val ex = assertThrows(IllegalStateException::class.java) {
//            runBlocking { FakeRecipeRepository.update(r) }
//        }
//        assertTrue(ex.message?.contains("ID must be greater than 0") == true)
//    }
//
//    @Test
//    fun `update throws when recipe not found`() = runBlocking {
//        val created = FakeRecipeRepository.create(sampleRecipe(title = "Will Not Find"))
//        val nonExisting = created.copy(id = 999)
//
//        val ex = assertThrows(IllegalArgumentException::class.java) {
//            runBlocking { FakeRecipeRepository.update(nonExisting) }
//        }
//        assertTrue(ex.message?.contains("Recipe with ID 999 not found") == true)
//    }
//
//    @Test
//    fun `delete removes recipe and returns true`() = runBlocking {
//        val created = FakeRecipeRepository.create(sampleRecipe(title = "To Delete"))
//
//        val deleted = FakeRecipeRepository.delete(created.id)
//        val found = FakeRecipeRepository.findById(created.id)
//
//        assertTrue(deleted)
//        assertNull(found)
//    }
//}

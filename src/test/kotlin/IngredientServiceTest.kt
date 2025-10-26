package service

import api.models.Allergens
import api.models.Ingredients
import api.repository.FakeIngredientsRepository
import api.service.IngredientService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IngredientServiceTest {

    private lateinit var ingredientService: IngredientService

    @BeforeEach
    fun setup() {
        // Reset global fake repository state before each test to avoid cross-test interference
        FakeIngredientsRepository.ingredients.clear()
        FakeIngredientsRepository.currentId = 0
        ingredientService = IngredientService()
    }

    @Test
    fun `create should assign ID and add ingredient`() = runBlocking {
        val ing = Ingredients(name = "Apple", description = "A red apple")

        val created = ingredientService.create(ing)

        assertEquals(1, created.id)
        assertEquals("Apple", created.name)
        assertEquals(1, FakeIngredientsRepository.ingredients.size)
    }

    @Test
    fun `findById returns correct ingredient`() = runBlocking {
        val created = ingredientService.create(Ingredients("Sugar", "Sweet"))

        val found = ingredientService.findById(created.id)

        assertEquals(created, found)
    }

    @Test
    fun `findByName returns ingredient`() = runBlocking {
        val peanutAllergen = Allergens(5, "PEANUTS", "Peanut", "May cause severe allergic reactions")
        val created = ingredientService.create(Ingredients("Peanut", "Nut", listOf(peanutAllergen)))

        val found = ingredientService.findByName("Peanut")

        assertEquals(created, found)
    }

    @Test
    fun `findAll returns all created ingredients`() = runBlocking {
        ingredientService.create(Ingredients("Lentils", "Protein"))
        ingredientService.create(Ingredients("Tomatoes", "Fresh"))

        val all = ingredientService.findAll()

        assertEquals(2, all.size)
        assertTrue(all.any { it.name == "Lentils" })
        assertTrue(all.any { it.name == "Tomatoes" })
    }

    @Test
    fun `updateAllergens updates allergens for existing ingredient`() = runBlocking {
        val created = ingredientService.create(Ingredients("Milk", "Dairy"))
        val newAllergens = listOf(Allergens(1, "MILK", "Milk", "Contains milk proteins"))

        val updated = ingredientService.updateAllergens(created, newAllergens)

        assertEquals(newAllergens, updated.allergens)
        // Ensure repository reflects the update
        val stored = ingredientService.findById(created.id)
        assertEquals(newAllergens, stored?.allergens)
    }

    @Test
    fun `updateAllergens throws when ID is zero`() = runBlocking {
        val ing = Ingredients("Sesame", "Seed") // id == 0

        val ex = assertThrows(IllegalStateException::class.java) {
            runBlocking { ingredientService.updateAllergens(ing, listOf(Allergens(3, "SESAME", "Sesame", "Sesame seeds"))) }
        }
        assertTrue(ex.message?.contains("ID must be greater than 0") == true)
    }

    @Test
    fun `updateAllergens throws when ingredient not found`() = runBlocking {
        val created = ingredientService.create(Ingredients("Spice", "Mix"))
        val nonExisting = created.copy(id = 999) // ensure ID does not exist in repo

        val ex = assertThrows(IllegalArgumentException::class.java) {
            runBlocking { ingredientService.updateAllergens(nonExisting, listOf(Allergens(7, "SOY", "Soy", "Contains soy"))) }
        }
        assertTrue(ex.message?.contains("Ingredient with ID 999 not found") == true)
    }

    @Test
    fun `update replaces existing ingredient when valid`() = runBlocking {
        val created = ingredientService.create(Ingredients("Tomato", "Red"))
        val modified = created.copy(description = "Juicy red tomato")

        ingredientService.update(modified)

        val found = ingredientService.findById(created.id)
        assertEquals("Juicy red tomato", found?.description)
    }

    @Test
    fun `update throws when ID is zero`() = runBlocking {
        val ing = Ingredients("Unknown", "No id") // id == 0

        val ex = assertThrows(IllegalStateException::class.java) {
            runBlocking { ingredientService.update(ing) }
        }
        assertTrue(ex.message?.contains("ID must be greater than 0") == true)
    }

    @Test
    fun `update throws when ingredient not found`() = runBlocking {
        val created = ingredientService.create(Ingredients("Apple", "Green"))
        val nonExisting = created.copy(id = 42)

        val ex = assertThrows(IllegalArgumentException::class.java) {
            runBlocking { ingredientService.update(nonExisting) }
        }
        // require without message will throw IllegalArgumentException without specific text
        assertNotNull(ex)
    }

    @Test
    fun `delete removes ingredient`() = runBlocking {
        val created = ingredientService.create(Ingredients("ToDelete", "Tmp"))

        val deleted = ingredientService.delete(created.id)
        val found = ingredientService.findById(created.id)

        assertTrue(deleted)
        assertNull(found)
    }
}

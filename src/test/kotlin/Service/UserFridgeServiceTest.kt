package service.Service

import io.mockk.*
import kotlinx.coroutines.runBlocking
import models.dto.UserFridgeEntry
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import repository.UserFridgeRepository
import service.UserFridgeService

class UserFridgeServiceTest {

    private lateinit var fridgeRepository: UserFridgeRepository
    private lateinit var fridgeService: UserFridgeService

    @BeforeEach
    fun setup() {
        fridgeRepository = mockk()
        fridgeService = UserFridgeService(fridgeRepository)
    }

    // UFS-01
    @Test
    fun `getUserFridgeEntries returns list of entries`() = runBlocking {
        val entries = listOf(
            UserFridgeEntry(userId = "1", ingredientName = "Tomato"),
            UserFridgeEntry(userId = "1", ingredientName = "Onion")
        )
        coEvery { fridgeRepository.getFridgeForUser("1") } returns entries

        val result = fridgeService.getUserFridgeEntries("1")

        assertEquals(2, result.size)
        assertEquals("Tomato", result[0].ingredientName)
        coVerify { fridgeRepository.getFridgeForUser("1") }
    }

    // UFS-02
    @Test
    fun `addUserFridgeEntry returns added entry`() = runBlocking {
        val entry = UserFridgeEntry(userId = "1", ingredientName = "Carrot")
        coEvery { fridgeRepository.addIngredient("1", "Carrot") } returns entry

        val result = fridgeService.addUserFridgeEntry(entry)

        assertEquals("Carrot", result.ingredientName)
        coVerify { fridgeRepository.addIngredient("1", "Carrot") }
    }

    // UFS-03
    @Test
    fun `removeUserFridgeEntry calls repository`() = runBlocking {
        coEvery { fridgeRepository.removeIngredient("1", "Lettuce") } just Runs

        fridgeService.removeUserFridgeEntry("1", "Lettuce")

        coVerify { fridgeRepository.removeIngredient("1", "Lettuce") }
    }
}
package service.Service

import io.mockk.*
import kotlinx.coroutines.runBlocking
import models.dto.UserAllergenEntry
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import repository.UserAllergensRepository
import service.UserAllergensService

class UserAllergenServiceTest {

    private lateinit var allergensRepository: UserAllergensRepository
    private lateinit var allergensService: UserAllergensService

    @BeforeEach
    fun setup() {
        allergensRepository = mockk()
        allergensService = UserAllergensService(allergensRepository)
    }

    @Test
    fun `getUserAllergenEntries returns list of entries`() = runBlocking {
        val entries = listOf(
            UserAllergenEntry(userId = "1", allergenId = 101),
            UserAllergenEntry(userId = "1", allergenId = 102)
        )
        coEvery { allergensRepository.getAllergensForUser("1") } returns entries

        val result = allergensService.getUserAllergenEntries("1")

        assertEquals(2, result.size)
        assertEquals(101, result[0].allergenId)
        coVerify { allergensRepository.getAllergensForUser("1") }
    }

    @Test
    fun `addUserAllergenEntry returns added entry`() = runBlocking {
        val entry = UserAllergenEntry(userId = "1", allergenId = 103)
        coEvery { allergensRepository.addAllergen("1", 103) } returns entry

        val result = allergensService.addUserAllergenEntry("1", 103)

        assertEquals(103, result.allergenId)
        coVerify { allergensRepository.addAllergen("1", 103) }
    }

    @Test
    fun `removeUserAllergenEntry calls repository`() = runBlocking {
        coEvery { allergensRepository.removeAllergen("1", 101) } just Runs

        allergensService.removeUserAllergenEntry("1", 101)

        coVerify { allergensRepository.removeAllergen("1", 101) }
    }

    @Test
    fun `checkAllergenExists returns true when allergen exists`() = runBlocking {
        val entries = listOf(UserAllergenEntry("1", 101))
        coEvery { allergensRepository.getAllergensForUser("1") } returns entries

        val result = allergensService.checkAllergenExists("1", 101)

        assertTrue(result)
        coVerify { allergensRepository.getAllergensForUser("1") }
    }

    @Test
    fun `checkAllergenExists returns false when allergen does not exist`() = runBlocking {
        val entries = listOf(UserAllergenEntry("1", 102))
        coEvery { allergensRepository.getAllergensForUser("1") } returns entries

        val result = allergensService.checkAllergenExists("1", 101)

        assertFalse(result)
        coVerify { allergensRepository.getAllergensForUser("1") }
    }
}
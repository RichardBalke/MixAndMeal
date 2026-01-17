package service.Service

import io.mockk.*
import kotlinx.coroutines.runBlocking
import models.dto.AllergenEntry
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import repository.AllergensRepositoryImpl
import service.AllergenService

class AllergenServiceTest {

    private lateinit var allergenRepository: AllergensRepositoryImpl
    private lateinit var allergenService: AllergenService

    @BeforeEach
    fun setup() {
        allergenRepository = mockk()
        allergenService = AllergenService(allergenRepository)
    }

    @Test
    fun `getAllergenById returns allergen when exists`() = runBlocking {
        val allergen = AllergenEntry(
            1,
            "Peanuts",
            "Peanuts Display",
            "Peanuts Description")
        coEvery { allergenRepository.findById(1) } returns allergen

        val result = allergenService.getAllergenById(1)

        assertNotNull(result)
        assertEquals("Peanuts", result?.name)
        coVerify { allergenRepository.findById(1) }
    }

    @Test
    fun `getAllergenById returns null when not found`() = runBlocking {
        coEvery { allergenRepository.findById(99) } returns null

        val result = allergenService.getAllergenById(99)

        assertNull(result)
        coVerify { allergenRepository.findById(99) }
    }
}
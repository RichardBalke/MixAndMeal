package service.Service

import io.mockk.*
import kotlinx.coroutines.runBlocking
import models.dto.DietEntry
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import repository.DietsRepositoryImpl
import service.DietsService

class DietsServiceTest {

    private lateinit var dietsRepository: DietsRepositoryImpl
    private lateinit var dietsService: DietsService

    @BeforeEach
    fun setup() {
        dietsRepository = mockk()
        dietsService = DietsService(dietsRepository)
    }

    @Test
    fun `getDietById returns diet`() = runBlocking {
        val diet = DietEntry(1, "Keto", "High fat, low carb")
        coEvery { dietsRepository.findByDietId(1) } returns diet

        val result = dietsService.getDietById(1)

        assertEquals("Keto", result?.displayName)
        assertEquals("High fat, low carb", result?.description)
        coVerify { dietsRepository.findByDietId(1) }
    }
}
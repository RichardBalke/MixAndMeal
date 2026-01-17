package service.Service

import io.mockk.*
import kotlinx.coroutines.runBlocking
import models.dto.DietEntry
import models.dto.UserDietEntry
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import repository.UserDietsRepository
import service.DietsService
import service.UserDietsService

class UserDietsServiceTest {

    private lateinit var dietsRepository: UserDietsRepository
    private lateinit var dietsService: UserDietsService

    @BeforeEach
    fun setup() {
        dietsRepository = mockk()
        dietsService = UserDietsService(dietsRepository)
    }

    @Test
    fun `getUserDietEntries returns list of entries`() = runBlocking {
        val entries = listOf(
            UserDietEntry(userId = "1", dietId = 101),
            UserDietEntry(userId = "1", dietId = 102)
        )
        coEvery { dietsRepository.getDietsForUser("1") } returns entries

        val result = dietsService.getUserDietEntries("1")

        assertEquals(2, result.size)
        assertEquals(101, result[0].dietId)
        coVerify { dietsRepository.getDietsForUser("1") }
    }

    @Test
    fun `addUserDietEntry returns added entry`() = runBlocking {
        val entry = UserDietEntry(userId = "1", dietId = 103)
        coEvery { dietsRepository.addDiet("1", 103) } returns entry

        val result = dietsService.addUserDietEntry(entry)

        assertEquals(103, result.dietId)
        coVerify { dietsRepository.addDiet("1", 103) }
    }

    @Test
    fun `removeUserDietEntry calls repository`() = runBlocking {
        coEvery { dietsRepository.removeDiet("1", 101) } just Runs

        dietsService.removeUserDietEntry("1", 101)

        coVerify { dietsRepository.removeDiet("1", 101) }
    }

    @Test
    fun `getDietsFromEntries returns list of DietEntry`() = runBlocking {
        val entries = listOf(
            UserDietEntry(userId = "1", dietId = 201),
            UserDietEntry(userId = "1", dietId = 202)
        )

        val dietsServiceMock = mockk<DietsService>()
        coEvery { dietsServiceMock.getDietById(201) } returns DietEntry(201, "Keto", "High fat, low carb")
        coEvery { dietsServiceMock.getDietById(202) } returns DietEntry(202, "Vegan", "Plant-based diet")

        val result = dietsService.getDietsFromEntries(entries, dietsServiceMock)

        assertEquals(2, result.size)
        assertEquals("Keto", result[0]!!.displayName)
        assertEquals("High fat, low carb", result[0]!!.description)
        assertEquals("Vegan", result[1]!!.displayName)
        assertEquals("Plant-based diet", result[1]!!.description)

        coVerify { dietsServiceMock.getDietById(201) }
        coVerify { dietsServiceMock.getDietById(202) }
    }
}
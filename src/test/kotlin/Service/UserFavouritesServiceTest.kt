package service.Service

import io.mockk.*
import kotlinx.coroutines.runBlocking
import models.dto.UserFavouritesEntry
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import repository.UserFavouritesRepository
import service.UserFavouritesService

class UserFavouritesServiceTest {

    private lateinit var favouritesRepository: UserFavouritesRepository
    private lateinit var favouritesService: UserFavouritesService

    @BeforeEach
    fun setup() {
        favouritesRepository = mockk()
        favouritesService = UserFavouritesService(favouritesRepository)
    }

    // USF-01
    @Test
    fun `getUserFavouritesEntries returns list of entries`() = runBlocking {
        val entries = listOf(
            UserFavouritesEntry(userId = "1", recipeId = 101),
            UserFavouritesEntry(userId = "1", recipeId = 102)
        )
        coEvery { favouritesRepository.getFavouritesForUser("1") } returns entries

        val result = favouritesService.getUserFavouritesEntries("1")

        assertEquals(2, result.size)
        assertEquals(101, result[0].recipeId)
        coVerify { favouritesRepository.getFavouritesForUser("1") }
    }

    // USF-02
    @Test
    fun `addUserFavouritesEntry returns added entry`() = runBlocking {
        val entry = UserFavouritesEntry(userId = "1", recipeId = 103)
        coEvery { favouritesRepository.addFavourite("1", 103) } returns entry

        val result = favouritesService.addUserFavouritesEntry(entry)

        assertEquals(103, result.recipeId)
        coVerify { favouritesRepository.addFavourite("1", 103) }
    }

    // USF-03
    @Test
    fun `removeUserFavouritesEntry calls repository`() = runBlocking {
        coEvery { favouritesRepository.removeFavourite("1", 101) } just Runs

        favouritesService.removeUserFavouritesEntry("1", 101)

        coVerify { favouritesRepository.removeFavourite("1", 101) }
    }

    // USF-04
    @Test
    fun `checkFavouriteExists returns true when favourite does not exist`() = runBlocking {
        coEvery { favouritesRepository.checkFavouriteExists("1", 104) } returns emptyList()

        val result = favouritesService.checkFavouriteExists("1", 104)

        assertTrue(result) // because repository returned empty → favourite does not exist
        coVerify { favouritesRepository.checkFavouriteExists("1", 104) }
    }

    // USF-05
    @Test
    fun `checkFavouriteExists returns false when favourite exists`() = runBlocking {
        coEvery { favouritesRepository.checkFavouriteExists("1", 105) } returns listOf(UserFavouritesEntry("1", 105))

        val result = favouritesService.checkFavouriteExists("1", 105)

        assertFalse(result)
        coVerify { favouritesRepository.checkFavouriteExists("1", 105) }
    }
}

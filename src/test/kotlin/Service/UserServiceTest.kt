package service.Service

import api.models.Role
import api.repository.UserRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import models.dto.UserEntry
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.UserService

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        userService = UserService(userRepository)
    }

    // USF-01
    @Test
    fun `getByEmail returns user when exists`() = runBlocking {
        val user = UserEntry(
            name = "Test user",
            email = "test@test.com",
            password = "pass",
            role = Role.USER)
        coEvery { userRepository.findByEmail("test@test.com") } returns user

        val result = userService.getByEmail("test@test.com")

        assertNotNull(result)
        assertEquals("test@test.com", result?.email)
        coVerify { userRepository.findByEmail("test@test.com") }
    }

    // USF-02
    @Test
    fun `getByEmail returns null when user does not exist`() = runBlocking {
        coEvery { userRepository.findByEmail("notfound@test.com") } returns null

        val result = userService.getByEmail("notfound@test.com")

        assertNull(result)
        coVerify { userRepository.findByEmail("notfound@test.com") }
    }

    // USF-03
    @Test
    fun `create calls repository and returns created user`() = runBlocking {
        val newUser = UserEntry(
            name = "New test user",
            email = "new@test.com",
            password = "pass",
            role = Role.USER
        )
        coEvery { userRepository.create(newUser) } returns newUser

        val result = userService.create(newUser)

        assertEquals(newUser, result)
        coVerify { userRepository.create(newUser) }
    }
}
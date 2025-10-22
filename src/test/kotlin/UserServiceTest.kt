package service

import api.models.User
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import api.repository.FakeUserRepository

class UserServiceTest {

    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        // Leeg de fake repository voor elke test
        FakeUserRepository.users.clear()
        FakeUserRepository.currentID = 0
        userService = InMemoryUserService()
    }

    @Test
    fun `create should assign ID and add user`() = runBlocking {
        val user = User(name = "Alice", password = "pw", email = "alice@mail.com")

        val created = userService.create(user)

        assertEquals(1, created.id)
        assertEquals("Alice", created.name)
        assertEquals(1, FakeUserRepository.users.size)
    }

    @Test
    fun `findById returns correct user`() = runBlocking {
        val user = userService.create(User(name = "Bob", password = "pw", email = "bob@mail.com"))

        val found = userService.findById(user.id!!)

        assertEquals(user, found)
    }

    @Test
    fun `findByUsername returns user`() = runBlocking {
        val user = userService.create(User(name = "Charlie", password = "pw", email = "c@mail.com"))

        val found = userService.findByUsername("Charlie")

        assertEquals(user, found)
    }

    @Test
    fun `delete removes user`() = runBlocking {
        val user = userService.create(User(name = "Dave", password = "pw", email = "d@mail.com"))

        val deleted = userService.delete(user.id!!)
        val found = userService.findById(user.id!!)

        assertTrue(deleted)
        assertNull(found)
    }
}

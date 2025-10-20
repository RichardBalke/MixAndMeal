package api.repository

import api.models.Allergens
import api.models.Recipes
import api.models.Role
import api.models.User
import kotlinx.coroutines.runBlocking
import service.UserService


interface UserRepository : CrudRepository<User, Long> {
    suspend fun findByUsername(username: String): User?
    suspend fun findByEmail(email: String): User?
    suspend fun addFavourite(userId: Long, recipe: Recipes) : User?
    suspend fun removeFavourite(userId: Long, recipe: Recipes): User?
    suspend fun updateAllergens(userId: Long, allergens: List<Allergens>) : List<Allergens>
    suspend fun getRoleById(id: Long): Role
}

object FakeUserRepository {
    public var currentID: Long = 0L
    public val user = UserService()
    public val users = mutableListOf<User>()

    init {
        runBlocking {
            user.create(User("Bart", "Test1", "test1@test.nl", Role.ADMIN))
            user.create(User("Fauve", "Test2", "test2@test.nl", Role.USER))
            user.create(User("Richard", "Test3", "test3@test.nl", Role.USER))
            user.create(User("Yoran", "Test4", "test4@test.nl", Role.USER))
        }
    }
}


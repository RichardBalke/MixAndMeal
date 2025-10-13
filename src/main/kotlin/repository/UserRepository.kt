package api.repository

import api.models.Role
import api.models.User
import kotlinx.coroutines.runBlocking


interface UserRepository : CrudRepository<User, Long> {
    suspend fun findByUsername(username: String): User?
}

object FakeUserRepository : UserRepository {
    private var currentID: Long = 0
    private val users = mutableListOf<User>()

    init {
        runBlocking {
            create(User("Bart", "koningRichard", "test1@test.nl", Role.USER))
            create(User("Fauve", "koningRichard", "test2@test.nl", Role.USER))
            create(User("Richard", "koningRichard", "test3@test.nl", Role.ADMIN))
            create(User("Yoran", "koningRichard", "test4@test.nl", Role.USER))
        }
    }

    // Creëert een user, voegt daar een ID aan toe en returned deze.
    override suspend fun create(entity: User): User {
        currentID++
        val newUser = entity.copy(id = currentID)
        users.add(newUser)
        return newUser
    }


    //Get functies
    override suspend fun findById(id: Long): User? {
        var output : User? = null
        for (user in users) {
            if (user.id == id) {
                output = user
            }
        }
        return output
//        return users.find { it.id == id }
    }

    override suspend fun findByUsername(username: String): User? {
        return users.find { it.name == username }
    }

    override suspend fun findAll(): List<User> {
        return users.toList()
    }
}


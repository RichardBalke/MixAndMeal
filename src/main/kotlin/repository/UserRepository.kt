package repository

import api.models.User

interface UserRepository {
    suspend fun findByUsername(username: String): User?
}

// Oude FakeUserRepository is uitgecomment, kan later eventueel teruggezet worden.
//object FakeUserRepository {
//    public var currentID: Long = 0L
//    public val user = UserService()
//    public val users = mutableListOf<User>()
//
//    init {
//        runBlocking {
//            user.create(User("Bart", "Test1", "test1@test.nl", Role.ADMIN))
//            user.create(User("Fauve", "Test2", "test2@test.nl", Role.USER))
//            user.create(User("Richard", "Test3", "test3@test.nl", Role.USER))
//            user.create(User("Yoran", "Test4", "test4@test.nl", Role.USER))
//        }
//    }
//}

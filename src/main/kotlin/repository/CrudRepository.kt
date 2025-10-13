package api.repository

interface CrudRepository <T,ID> {
    suspend fun findById(id: ID): T?
    suspend fun create(entity: T): T
    suspend fun findAll(): List<T>

}


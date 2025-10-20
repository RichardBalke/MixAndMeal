package service

import api.models.Allergens
import api.models.User
import api.models.Recipes
import api.models.Ingredients
import api.repository.FakeUserRepository.users
import api.repository.FakeUserRepository.currentID
import api.repository.UserRepository


class UserService : UserRepository {

    // Creëert een user, voegt daar een ID aan toe en returned deze.
    override suspend fun create(entity: User): User {
        currentID++
        val newUser = entity.copy(id = currentID)
        users.add(newUser)
        return newUser
    }


    //Get functies
    override suspend fun findById(id: Long): User? {
        return users.find { it.id == id }
    }

    override suspend fun findByUsername(username: String): User? {
        return users.find { it.name == username }
    }

    override suspend fun findByEmail(email: String): User? {
        return users.find { it.email == email }
    }

    override suspend fun findAll(): List<User> {
        return users.toList()
    }

    override suspend fun delete(id: Long): Boolean {
        return users.removeIf { id == it.id}
    }

    override suspend fun update(entity: User) {
        val index = users.indexOfFirst { it.id == entity.id }
        if (index != -1) {
            users[index] = entity
        }
    }

    override suspend fun addFavourite(userId: Long, recipe: Recipes): User? {
        val user = findById(userId)
        if (user != null) {
            if (user.favourites.any { it.id == recipe.id }) {
                val updatedUser = user.copy(favourites = user.favourites + recipe)
                update(updatedUser)
                return updatedUser
            } else {
                return user
            }
        } else {
            return null
        }
    }

    override suspend fun removeFavourite(userId: Long, recipe: Recipes): User? {
        val user = findById(userId)
        if (user != null) {
            if (user.favourites.any { it.id == recipe.id }) {
                val updatedFavourites = user.favourites.filterNot { it.id == recipe.id }
                val updatedUser = user.copy(favourites = updatedFavourites)
                update(updatedUser)
                return updatedUser
            } else {
                return user
            }
        } else {
            return null
        }
    }


    override suspend fun updateAllergens(userId: Long, allergens: List<Allergens>): List<Allergens> {
        val user = findById(userId)
            ?: throw NoSuchElementException("User with ID $userId not found")
        val updatedUser = user.copy(allergens = allergens)
        val index = users.indexOfFirst { it.id == userId }
        if (index != -1) {
            users[index] = updatedUser
        } else {
            throw IllegalStateException("User list out of sync — user not found in repository.")
        }
        return updatedUser.allergens
    }


//  Functies voor mogelijke fridge/pantry in de user profile

//    suspend fun addIngredient(userId: Long, ingredient: Ingredients): Boolean {
//        val user = findById(userId) ?: return false
//        if (user.list.any { it.name == ingredient.name }) return false
//        val updatedUser = user.copy(list = user.list + ingredient)
//        update(updatedUser)
//        return true
//    }
//
//    suspend fun removeIngredient(userId: Long, ingredientName: String): Boolean {
//        val user = findById(userId) ?: return false
//        val updatedList = user.list.filterNot { it.name == ingredientName }
//        val updatedUser = user.copy(list = updatedList)
//        update(updatedUser)
//        return true
//    }



}
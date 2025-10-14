package api.repository

import api.models.Allergens
import api.models.Ingredients
import kotlinx.coroutines.runBlocking

interface IngredientsRepository : CrudRepository<Ingredients, Long> {
    suspend fun findByName(name: String): Ingredients?
    suspend fun updateAllergens(entity : Ingredients, newAllergens : List<Allergens>) : Ingredients
}

object FakeIngredientsRepository : IngredientsRepository {
    private var currentId : Long = 0
    private val ingredients : MutableList<Ingredients> = mutableListOf()

    init{
        runBlocking {
            create(Ingredients("Apple", "A red apple"))
            create(Ingredients("Sugar", "A sweet substance"))
            create(Ingredients("Peanut", "A tasty nut", listOf(Allergens.PEANUTS)))
        }
    }

    override suspend fun create(entity: Ingredients): Ingredients {
        currentId++
        val newIngredient = entity.copy(id = currentId)
        ingredients.add(newIngredient)
        return newIngredient
    }

    override suspend fun updateAllergens(entity: Ingredients, newAllergens: List<Allergens>): Ingredients {
        check(entity.id > 0) { "ID must be greater than 0." }
        val index = ingredients.indexOfFirst { it.id == entity.id }
        require(index != -1) { "Ingredient with ID ${entity.id} not found." }
        val updated = entity.copy(allergens = newAllergens)
        ingredients[index] = updated
        return updated
    }

    override suspend fun delete(id: Long): Boolean {
        return ingredients.removeIf { id == it.id }
    }

    override suspend fun update(entity: Ingredients) {
        check(entity.id > 0) { "ID must be greater than 0." }
        require(ingredients.any { it.id == entity.id })
        ingredients.removeIf { entity.id == it.id }
        ingredients.add(entity)
    }

    override suspend fun findByName(name: String): Ingredients?{
        return ingredients.find { it.name == name }
    }

    override suspend fun findById(id: Long): Ingredients?{
        return ingredients.find { it.id == id }
    }

    override suspend fun findAll(): List<Ingredients> {
        return ingredients.toList()
    }
}

package api.repository

import api.models.Allergens
import api.models.Ingredients
import kotlinx.coroutines.runBlocking
import api.service.IngredientService

interface IngredientsRepository : CrudRepository<Ingredients, Long> {
    suspend fun findByName(name: String): Ingredients?
    suspend fun updateAllergens(entity : Ingredients, newAllergens : List<Allergens>) : Ingredients
}

object FakeIngredientsRepository {
    public var currentId : Long = 0
    public val ingredients : MutableList<Ingredients> = mutableListOf()
    private val service = IngredientService()

    init{
        runBlocking {
            service.create(Ingredients("Apple", "A red apple"))
            service.create(Ingredients("Sugar", "A sweet substance"))
            service.create(Ingredients("Peanut", "A tasty nut", listOf(Allergens.PEANUTS)))
            service.create(Ingredients("Lentils", "Hearty and protein-rich lentils"))
            service.create(Ingredients("Tomatoes", "Fresh ripe tomatoes"))
            service.create(Ingredients("Spices", "A mix of Indian spices"))
        }
    }


}

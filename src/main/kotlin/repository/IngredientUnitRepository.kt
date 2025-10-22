package api.repository


import api.models.IngredientUnits
import api.models.Ingredients
import api.service.IngredientService
import kotlinx.coroutines.runBlocking
import kotlin.math.E

interface IngredientUnitRepository {

}

object FakeIngredientUnitRepository : IngredientUnitRepository {

    val list = mutableListOf<IngredientUnits>()
    val ingredientService = IngredientService()

    init {
        runBlocking {
            val ingredient1 = FakeIngredientUnitRepository.create(1, 1.5, "KG")
            val ingredient2 = FakeIngredientUnitRepository.create(2, 50.0, "GRAMS")
            val ingredient3 = FakeIngredientUnitRepository.create(3, 10.0, "GRAMS")
            val ingredient4 = FakeIngredientUnitRepository.create(4, 0.2, "KG")   // Lentils: 200 grams
            val ingredient5 = FakeIngredientUnitRepository.create(5, 3.0, "GRAMS") // Tomatoes: 3 pieces
            val ingredient6 = FakeIngredientUnitRepository.create(6, 2.0, "GRAMS")  // Spices: 2 tablespoons
        }
    }

    suspend fun create(id: Long, amount: Double, unitType: String): IngredientUnits? {

        val findIngredient = ingredientService.findById(id)
        if (findIngredient == null) {
            IllegalArgumentException()
            return null
        } else {
            val newIngredientUnit = IngredientUnits(findIngredient, amount, unitType)
            list.add(newIngredientUnit)
            return newIngredientUnit
        }
    }
}

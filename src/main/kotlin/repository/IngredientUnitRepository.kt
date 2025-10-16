package api.repository


import api.models.IngredientUnits
import api.models.Ingredients
import kotlinx.coroutines.runBlocking
import kotlin.math.E

interface IngredientUnitRepository {

}

object FakeIngredientUnitRepository : IngredientUnitRepository {

    suspend fun create(id: Long, amount: Double, unitType: String): IngredientUnits? {

        val findIngredient = FakeIngredientsRepository.findById(id)
        if (findIngredient == null) {
            IllegalArgumentException()
            return null
        } else {
            return IngredientUnits(findIngredient, amount, unitType)
        }
    }
}

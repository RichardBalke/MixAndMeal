package repository

import api.repository.CrudImplementation
import api.repository.CrudRepository
import models.dto.RecipeAllergenEntry
import models.dto.RecipeDietEntry
import models.tables.RecipeAllergens
import models.tables.RecipeImages
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface RecipeAllergensRepository : CrudRepository<RecipeAllergenEntry, RecipeAllergenEntry> {
    suspend fun findAllByRecipeId(recipeId: Int): List<RecipeAllergenEntry>
    suspend fun findAllByAllergenId(allergenId: Int) : List<RecipeAllergenEntry>
    suspend fun deleteDietsByRecipeId(recipeId: Int): Int
}

class RecipeAllergensRepositoryImpl() : CrudImplementation<RecipeAllergenEntry, RecipeAllergenEntry>
    (
        table = RecipeAllergens,
        toEntity = { row ->
            RecipeAllergenEntry(row[RecipeAllergens.recipeId],
                row[RecipeAllergens.allergenId]) },
        idColumns = listOf(RecipeAllergens.recipeId, RecipeAllergens.allergenId),
        idExtractor = { entry -> listOf(entry.recipeId, entry.allergenId)},
        entityMapper = {stmt, ingredientAllergen ->
            stmt[RecipeAllergens.recipeId] = ingredientAllergen.recipeId
            stmt[RecipeAllergens.allergenId] = ingredientAllergen.allergenId
        }), RecipeAllergensRepository {

    override suspend fun findAllByRecipeId(recipeId: Int): List<RecipeAllergenEntry> = transaction{
        table.selectAll()
            .where { RecipeAllergens.recipeId eq recipeId }
            .map(toEntity)
            .toList()
    }

    override suspend fun findAllByAllergenId(allergenId: Int): List<RecipeAllergenEntry> = transaction {
        table.selectAll()
            .where { RecipeAllergens.allergenId eq allergenId }
            .map(toEntity)
        .   toList()
    }

    override suspend fun deleteDietsByRecipeId(recipeId: Int): Int = transaction {
        table.deleteWhere { RecipeAllergens.recipeId eq recipeId }
    }

}
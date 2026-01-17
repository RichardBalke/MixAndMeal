package api.repository

import models.dto.IngredientUnitEntry
import models.dto.RecipeImageEntry
import models.tables.IngredientUnits
import models.tables.RecipeImages
import models.tables.Recipes
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import responses.RecipeSearchResult

interface IngredientUnitRepository : CrudRepository<IngredientUnitEntry, IngredientUnitEntry> {
 suspend fun findRecipesByIngredient(ingredientName : String): List<RecipeSearchResult>
 suspend fun findAllByRecipeId(recipeId : Int): List<IngredientUnitEntry>
 suspend fun deleteIngredientsByRecipeId(recipeId: Int): Int
}

class IngredientUnitRepositoryImpl() : IngredientUnitRepository,
    CrudImplementation<IngredientUnitEntry, IngredientUnitEntry>(
    table = IngredientUnits,
    toEntity = { row ->
        IngredientUnitEntry(row[IngredientUnits.recipeId],
            row[IngredientUnits.ingredientName],
            row[IngredientUnits.amount],
            row[IngredientUnits.unitType]
        )},
    idColumns = listOf(IngredientUnits.recipeId, IngredientUnits.ingredientName),
    idExtractor = {entry -> listOf(entry.recipeId, entry.ingredientName)},
    entityMapper = { stmt, ingredientUnit ->
        stmt[IngredientUnits.recipeId] = ingredientUnit.recipeId
        stmt[IngredientUnits.ingredientName] = ingredientUnit.ingredientName
        stmt[IngredientUnits.amount] = ingredientUnit.amount
        stmt[IngredientUnits.unitType] = ingredientUnit.unitType
    }
) {
    override suspend fun findRecipesByIngredient(ingredientName: String): List<RecipeSearchResult> {
        val recipeIds = transaction {
            IngredientUnits.selectAll()
                .where { IngredientUnits.ingredientName like ingredientName }
                .map { row -> row[IngredientUnits.recipeId] }
        }
        val foundRecipes = mutableListOf<RecipeSearchResult>()
        for (recipe in recipeIds) {
            val search = transaction {
                Recipes.selectAll()
                    .where { Recipes.id eq recipe }
                    .map { row ->
                        RecipeSearchResult(
                            row[Recipes.id],
                            row[Recipes.title],
                            row[Recipes.description],
                            row[Recipes.cookingTime],
                            listOf<RecipeImageEntry>(),
                            0.0
                        )
                    }
            }
            foundRecipes.addAll(search)
        }
        return foundRecipes
    }

    override suspend fun findAllByRecipeId(recipeId: Int): List<IngredientUnitEntry> = transaction {
        IngredientUnits.selectAll()
            .where { IngredientUnits.recipeId eq recipeId }
            .map(toEntity)
            .toList()
    }

    override suspend fun deleteIngredientsByRecipeId(recipeId: Int): Int = transaction {
        table.deleteWhere { IngredientUnits.recipeId eq recipeId }
    }
}

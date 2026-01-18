package api.repository

import api.responses.RecipeCardResponse
import models.enums.Difficulty
import models.enums.KitchenStyle
import models.enums.MealType
import models.dto.RecipeEntry
import models.dto.RecipeImageEntry
import models.dto.UserFavouritesEntry
import models.tables.Recipes
import models.tables.RecipeDiets
import models.tables.IngredientUnits
import models.tables.RecipeAllergens
import models.tables.RecipeImages
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.orWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import requests.RecipeSearchRequest

interface RecipesRepository : CrudRepository<RecipeEntry, Int>{
    suspend fun findByRecipeId(recipeId: Int): RecipeEntry?
    suspend fun findPopularRecipes(limit: Int): List<RecipeCardResponse>
    suspend fun findRecipeCardsByDifficulty(limit: Int, difficulty: String): List<RecipeCardResponse>
    suspend fun findQuickRecipes(limit: Int): List<RecipeCardResponse>
    suspend fun findFavoriteRecipes(recipeIds: List<UserFavouritesEntry>): List<RecipeCardResponse>
    suspend fun searchRecipes(recipeSearchRequest: RecipeSearchRequest) : Set<RecipeCardResponse>
}


class RecipesRepositoryImpl : RecipesRepository, CrudImplementation<RecipeEntry, Int>(
    table = Recipes,
    toEntity = { row ->
        val difficultyString = row[Recipes.difficulty]
        val difficultyEnum = Difficulty.valueOf(difficultyString)
        val mealTypeString = row[Recipes.mealType]
        val mealTypeEnum = MealType.valueOf(mealTypeString)
        val kitchenStyleString = row[Recipes.kitchenStyle]
        val kitchenStyleEnum = KitchenStyle.valueOf(kitchenStyleString)
        RecipeEntry(row[Recipes.id],
            row[Recipes.title],
            row[Recipes.description],
            row[Recipes.instructions],
            row[Recipes.prepTime],
            row[Recipes.cookingTime],
            difficultyEnum,
            mealTypeEnum,
            kitchenStyleEnum,
            row[Recipes.favoritesCount]) },
    idColumns = listOf(Recipes.id),
    idExtractor =  { entry -> listOf(entry) },
    entityMapper = { stmt, recipe ->
//        stmt[Recipes.id] = recipe.id?.toInt() ?: 0
        stmt[Recipes.title] = recipe.title
        stmt[Recipes.description] = recipe.description
        stmt[Recipes.instructions] = recipe.instructions
        stmt[Recipes.prepTime] = recipe.prepTime
        stmt[Recipes.cookingTime] = recipe.cookingTime
        stmt[Recipes.difficulty] = recipe.difficulty.name
        stmt[Recipes.mealType] = recipe.mealType.name
        stmt[Recipes.kitchenStyle] = recipe.kitchenStyle.name
        stmt[Recipes.favoritesCount] = recipe.favoritesCount
    }){

    override suspend fun findByRecipeId(recipeId: Int): RecipeEntry? = transaction {
        table.selectAll()
            .where(Recipes.id eq recipeId)
            .mapNotNull(toEntity)
            .firstOrNull()
    }

    override suspend fun findPopularRecipes(limit: Int): List<RecipeCardResponse> = transaction {
        table.select(Recipes.id, Recipes.title, Recipes.description, Recipes.cookingTime, Recipes.favoritesCount)
            .orderBy(Recipes.favoritesCount, SortOrder.DESC)
            .limit(limit)
            .map {RecipeCardResponse(
                recipeId = it[Recipes.id],
                it[Recipes.title],
                it[Recipes.description],
                it[Recipes.cookingTime],
                mutableListOf()
            )
            }
            .toList()
    }

    override suspend fun findRecipeCardsByDifficulty(limit: Int, difficulty: String): List<RecipeCardResponse> = transaction {
        table.select(Recipes.id, Recipes.title, Recipes.description, Recipes.cookingTime)
            .where(Recipes.difficulty eq difficulty)
            .limit(limit)
            .map{RecipeCardResponse(
                recipeId = it[Recipes.id],
                it[Recipes.title],
                it[Recipes.description],
                it[Recipes.cookingTime],
                mutableListOf())
            }
            .toList()
    }

    override suspend fun findQuickRecipes(limit: Int): List<RecipeCardResponse> = transaction {
        table.select(Recipes.id, Recipes.title, Recipes.description, Recipes.cookingTime)
            .where(Recipes.cookingTime.lessEq(30))
            .orderBy(Recipes.cookingTime)
            .limit(limit)
            .map{RecipeCardResponse(
                recipeId = it[Recipes.id],
                it[Recipes.title],
                it[Recipes.description],
                it[Recipes.cookingTime],
                mutableListOf())
            }
            .toList()
    }

    override suspend fun findFavoriteRecipes(recipeIds: List<UserFavouritesEntry>): List<RecipeCardResponse> {
        val list = mutableListOf<RecipeCardResponse>()
        for(recipeId in recipeIds){
            list.addAll(
                transaction { table.select(Recipes.id, Recipes.title, Recipes.description, Recipes.cookingTime)
                    .where(Recipes.id eq recipeId.recipeId)
                    .map{RecipeCardResponse(
                        recipeId = it[Recipes.id],
                        it[Recipes.title],
                        it[Recipes.description],
                        it[Recipes.cookingTime],
                        mutableListOf())
                    } }
            )
        }
        return list
    }

    override suspend fun searchRecipes(recipeSearchRequest: RecipeSearchRequest) : Set<RecipeCardResponse> = transaction {
        val query = (Recipes leftJoin RecipeDiets leftJoin RecipeAllergens leftJoin IngredientUnits leftJoin RecipeImages)
            .selectAll()
        recipeSearchRequest.partialTitle?.let { term ->
            query.orWhere {
                Recipes.title.lowerCase() like "%${term.lowercase()}%"
            }
        }
        recipeSearchRequest.difficulty?.let {query.orWhere { Recipes.difficulty eq it.uppercase() }}
        recipeSearchRequest.mealType?.let {query.orWhere { Recipes.mealType eq it.uppercase() }}
        recipeSearchRequest.kitchenStyle?.let {query.orWhere { Recipes.kitchenStyle eq it.uppercase() }}
        recipeSearchRequest.maxCookingTime?.let {query.orWhere { Recipes.cookingTime lessEq it }}
        if (recipeSearchRequest.diets.isNotEmpty()) {
            query.orWhere { RecipeDiets.dietId inList recipeSearchRequest.diets }
        }
        if(recipeSearchRequest.allergens.isNotEmpty()) {
            recipeSearchRequest.allergens.let { query.orWhere { RecipeAllergens.allergenId inList it } }
        }
        if(recipeSearchRequest.ingredients.isNotEmpty()) {
            recipeSearchRequest.ingredients.let { query.orWhere { IngredientUnits.ingredientName inList it } }
        }

            query.map{
                RecipeCardResponse(
                    recipeId = it[Recipes.id],
                    title = it[Recipes.title],
                    description = it[Recipes.description],
                    cookingTime = it[Recipes.cookingTime],
                    imageUrl = mutableListOf(
                        RecipeImageEntry(
                            it[RecipeImages.id],
                            it[RecipeImages.recipeId],
                            it[RecipeImages.imageUrl]))
                )}.toSet()
    }


}

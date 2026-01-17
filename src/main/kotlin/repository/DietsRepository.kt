package repository

import api.repository.CrudImplementation
import api.repository.CrudRepository
import models.dto.DietEntry
import models.tables.Diets
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface DietsRepository : CrudRepository<DietEntry, Int> {
    suspend fun findByDietId(dietId: Int) : DietEntry?
    suspend fun findByDisplayName(displayName: String): DietEntry?
}

class DietsRepositoryImpl() : DietsRepository, CrudImplementation<DietEntry, Int>(
    table = Diets,
    toEntity = {row ->
        DietEntry(row[Diets.id], row[Diets.displayName], row[Diets.description])
    },
    idColumns = listOf(Diets.id),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = {stmt, diet ->
        stmt[Diets.id] = diet.id
        stmt[Diets.displayName] = diet.displayName
        stmt[Diets.description] = diet.description
    }
) {
    override suspend fun findByDietId(dietId: Int) : DietEntry? = transaction {
        table.selectAll()
            .where(Diets.id eq dietId)
            .map(toEntity)
            .singleOrNull()
    }

    override suspend fun findByDisplayName(displayName: String): DietEntry? = transaction {
        table.selectAll()
            .where(Diets.displayName eq displayName).firstNotNullOfOrNull(toEntity)
    }
}
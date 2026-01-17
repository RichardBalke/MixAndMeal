package api.repository

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsert

interface CrudRepository <T,ID> {
    suspend fun findById(id: ID): T?
    suspend fun create(entity: T): T
    suspend fun findAll(): List<T>
    suspend fun update(id: ID, entity: T): T
    suspend fun delete(id: ID): Boolean
}

abstract class CrudImplementation<T : Any, ID : Any>(
    protected val table: Table,
    protected val toEntity: (ResultRow) -> T,
    protected val idColumns: List<Column<*>>, // List instead of single column
    protected val idExtractor: (ID) -> List<Any>, // Extracts PK values from ID
    protected val entityMapper: (UpdateBuilder<*>, T) -> Unit
) : CrudRepository<T, ID> {

    // Helper to build composite WHERE clause
    protected fun compositeEq(id: ID): Op<Boolean> {
        val values = idExtractor(id)
        return idColumns
            .zip(values)
            .map { (col, value) -> col as Column<Any> eq value }
            .reduce { acc, op -> acc and op }
    }

    override suspend fun findById(id: ID): T? = transaction {
        table.selectAll()
            .where { compositeEq(id) }
            .mapNotNull(toEntity)
            .singleOrNull()
    }

    override suspend fun findAll(): List<T> = transaction {
        table.selectAll()
            .map(toEntity)
            .toList()
    }

    override suspend fun update(id: ID, entity: T): T = transaction {
        val updatedRows = table.update({ compositeEq(id) }) {
            entityMapper(it, entity)
        }
        require(updatedRows > 0) { "No rows updated" }
        val updatedEntityRow = table.selectAll()
            .where { compositeEq(id) }
            .single()
        toEntity(updatedEntityRow)
    }

    override suspend fun create(entity: T): T = transaction {
        val inserted = table.upsert {
            entityMapper(it, entity)
        }
        toEntity(inserted.resultedValues!!.first())
    }

    override suspend fun delete(id: ID): Boolean = transaction {
        table.deleteWhere { compositeEq(id) } > 0
    }
}



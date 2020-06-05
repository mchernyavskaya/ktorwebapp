package com.example.com.example

import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction

class PersonEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PersonEntity>(PersonTable)

    var name by PersonTable.name
    var birthYear by PersonTable.birthYear
}

object PersonTable : IntIdTable() {
    val name = varchar("name", 100).index()
    val birthYear = integer("birthYear")
}

@KtorExperimentalAPI
class PersonRepository {

    fun get(id: Int): PersonEntity? = transaction() {
        PersonEntity.findById(id)
    }

    fun getAll(): List<PersonEntity> = transaction() {
        PersonEntity.all().sortedBy { it.name }.toList()
    }

    fun create(id: Int? = null, name: String, birthYear: Int): PersonEntity {
        return transaction() {
            PersonEntity.new(id) {
                this.name = name
                this.birthYear = birthYear
            }
        }
    }

    fun update(id: Int, name: String, birthYear: Int): PersonEntity {
        return transaction() {
            PersonEntity.findById(id)!!.let {
                it.name = name
                it.birthYear = birthYear
                return@transaction it
            }
        }
    }

    fun delete(id: Int) {
        transaction() {
            PersonEntity.findById(id)?.delete()
        }
    }
}

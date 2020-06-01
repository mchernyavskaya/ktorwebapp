package com.example.com.example

import com.example.configuration.Database
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

    /**
     * Instead of mappers, just added a transformation method
     */
    fun toDto(): Person {
        return Person(id.value, name, birthYear)
    }
}

object PersonTable : IntIdTable() {
    val name = varchar("name", 100).index()
    val birthYear = integer("birthYear")
}

@KtorExperimentalAPI
object PersonRepository {

    fun get(id: Int): Person? = transaction(Database.connection) {
        PersonEntity.findById(id)?.toDto()
    }

    fun getAll(): List<PersonEntity> = transaction(Database.connection) {
        PersonEntity.all().toList()
    }

    fun create(person: Person): Person {
        val created = transaction(Database.connection) {
            PersonEntity.new(person.id) {
                name = person.name
                birthYear = person.birthYear
            }
        }
        return created.toDto()
    }

    fun update(person: Person): Person {
        val updated = transaction(Database.connection) {
            PersonEntity.findById(person.id!!)!!.let {
                it.name = person.name
                it.birthYear = person.birthYear
                return@transaction it
            }
        }
        return updated.toDto()
    }

    fun delete(id: Int) {
        PersonEntity.findById(id)?.delete()
    }
}

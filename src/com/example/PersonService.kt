package com.example.com.example

import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
class PersonService(val repository: PersonRepository) {
    fun get(id: Int): Person? = Person.fromEntity(repository.get(id))

    fun getAll(): List<Person> = repository.getAll().map { Person.fromEntity(it)!! }

    fun create(person: Person): Person = Person.fromEntity(
        repository.create(id = person.id, name = person.name, birthYear = person.birthYear)
    )!!

    fun update(person: Person): Person = Person.fromEntity(
        repository.update(id = person.id!!, name = person.name, birthYear = person.birthYear)
    )!!

    fun delete(id: Int) = repository.delete(id)
}

package com.example.com.example

data class Person(
    val id: Int? = null,
    val name: String,
    val birthYear: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Person

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        fun fromEntity(p: PersonEntity?): Person? {
            if (p == null) {
                return null
            }
            return Person(
                id = p.id.value,
                name = p.name,
                birthYear = p.birthYear
            )
        }
    }
}

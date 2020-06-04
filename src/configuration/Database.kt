package com.example.configuration

import com.example.com.example.PersonTable
import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

@KtorExperimentalAPI
class Database(
    val application: Application,
    val testing: Boolean = false
) {
    companion object {
        var connection: Database? = null
    }

    fun connect() {
        if (connection == null) {
            val host = getDbProperty("host")
            val port = getDbProperty("port")
            val database = getDbProperty("name")
            val user = getDbProperty("user")
            val pass = getDbProperty("pass")
            connection = if (testing) {
                Database.connect(
                    url = "jdbc:tc:mysql://$host:$port/$database",
                    driver = "org.testcontainers.jdbc.ContainerDatabaseDriver",
                    user = user,
                    password = pass
                )
            } else {
                Database.connect(
                    url = "jdbc:mysql://$host:$port/$database",
                    driver = "com.mysql.jdbc.Driver",
                    user = user,
                    password = pass
                )
            }
        }
        transaction(connection) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.createMissingTablesAndColumns(PersonTable)
        }
    }

    private fun getDbProperty(name: String): String = application.environment
        .config.property("ktor.database.$name").getString()
}

package com.example.configuration

import com.example.com.example.PersonTable
import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.containers.MySQLContainer

@KtorExperimentalAPI
class Database(
    private val application: Application,
    private val testing: Boolean = false
) {
    companion object {
        var connection: Database? = null
        private var testContainer: ConcreteMySQLContainer? = null
    }

    fun connect() {
        if (connection == null) {
            val host = getDbProperty("host")
            val port = getDbProperty("port")
            val database = getDbProperty("name")
            val user = getDbProperty("user")
            val pass = getDbProperty("pass")
            connection = if (testing) {
                startTestContainer()
                Database.connect(
                    url = testContainer!!.jdbcUrl,
                    driver = testContainer!!.driverClassName,
                    user = testContainer!!.username,
                    password = testContainer!!.password
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

    fun cleanup() {
        testContainer?.stop()
    }

    private fun startTestContainer() {
        testContainer = if (testing) ConcreteMySQLContainer() else null
        testContainer?.start()
    }

    private fun getDbProperty(name: String): String = application.environment
        .config.property("ktor.database.$name").getString()
}

internal class ConcreteMySQLContainer() : MySQLContainer<ConcreteMySQLContainer>()

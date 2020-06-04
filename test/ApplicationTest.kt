package com.example

import io.ktor.config.MapApplicationConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlin.test.Test
import kotlin.test.assertEquals

@KtorExperimentalAPI class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({
            applyTestEnv(environment.config as MapApplicationConfig)
            module(testing = true)
        }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

    @Test
    fun testCreatePerson() {
        withTestApplication({
            applyTestEnv(environment.config as MapApplicationConfig)
            module(testing = true)
        }) {
            handleRequest(HttpMethod.Post, "/person") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    """
                    { "id": 3, "name" : "Maryna Cherniavska", "birthYear" : 1981 }
                """.trimIndent()
                )
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
            }
            handleRequest(HttpMethod.Get, "/person").apply {
                assert(response.content!!.contains("Maryna Cherniavska"))
                assert(response.content!!.contains("1981"))
            }
        }
    }

    @Test
    fun testUpdatePerson() {
        withTestApplication({
            applyTestEnv(environment.config as MapApplicationConfig)
            module(testing = true)
        }) {
            handleRequest(HttpMethod.Post, "/person") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    """
                    { "id" : 3, "name" : "Maryna", "birthYear" : 1982 }
                """.trimIndent()
                )
            }.apply {
                assertEquals(HttpStatusCode.Accepted, response.status())
            }
            handleRequest(HttpMethod.Get, "/person").apply {
                assert(response.content!!.contains("Maryna"))
                assert(!response.content!!.contains("Cherniavska"))
                assert(response.content!!.contains("1982"))
            }
        }
    }

    @Test
    fun testDeletePerson() {
        withTestApplication({
            applyTestEnv(environment.config as MapApplicationConfig)
            module(testing = true)
        }) {
            handleRequest(HttpMethod.Delete, "/person/3").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            handleRequest(HttpMethod.Get, "/person/3").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    private fun applyTestEnv(config: MapApplicationConfig) {
        config.apply {
            // Set custom properties
            put("ktor.database.host", "localhost")
            put("ktor.database.port", "3306")
            put("ktor.database.name", "test")
            put("ktor.database.user", "test")
            put("ktor.database.pass", "test")
        }
    }
}

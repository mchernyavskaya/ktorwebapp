package com.example

import com.example.com.example.Person
import com.example.com.example.PersonRepository
import com.example.com.example.PersonService
import com.example.configuration.Database
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.path
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
val appModule = module {
    single { PersonService(get()) }
    single { PersonRepository() }
}

@KtorExperimentalAPI
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    log.info("Starting the application with testing=$testing")

    install(DefaultHeaders)
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(Koin) {
        modules(appModule)
    }

    Database(this, testing).connect()

    val personService: PersonService by inject()

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/person") {
            call.respond(personService.getAll())
        }

        get("/person/{id}") {
            call.parameters["id"]?.toInt()?.let {
                val person = personService.get(it)
                if (person != null) {
                    call.respond(person)
                    return@get
                }
            }
            call.respond(HttpStatusCode.NotFound)
        }

        post("/person") {
            val person = call.receive(Person::class)
            val created = personService.create(person)
            log.info("Created: $created")
            call.respond(HttpStatusCode.OK)
        }

        put("/person") {
            val person = call.receive(Person::class)
            val updated = personService.update(person)
            log.info("Updated: $updated")
            call.respond(HttpStatusCode.OK)
        }

        delete("/person/{id}") {
            call.parameters["id"]?.toInt()?.let {
                personService.delete(it)
                call.respond(HttpStatusCode.OK)
                return@delete
            }
        }
    }
}


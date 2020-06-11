# A Kotlin Web Microservice without Spring Boot

This is an example of a REST API in Kotlin without using Spring Boot.
It's basically an exercise to try alternative libraries.

The application was used for a conference talk, the slides of which are [here](https://bit.ly/WinterComingSlides).

The application development stages are shown in different branches:

1. `master` - this branch; contains all the changes from the last branch.
2. [`1-fatjar`](https://github.com/mchernyavskaya/ktorwebapp/tree/1-fatjar) - the initial [Ktor](https://ktor.io/) application with the added custom data class and *shadow jar* (fat jar) Gradle task.
3. [`2-rest`](https://github.com/mchernyavskaya/ktorwebapp/tree/2-rest) - implemented the CRUD endpoints.
4. [`3-persistence`](https://github.com/mchernyavskaya/ktorwebapp/tree/3-persistence) - added database interaction (mysql) with [Jetbrains Exposed](https://github.com/JetBrains/Exposed).
5. [`4-di`](https://github.com/mchernyavskaya/ktorwebapp/tree/4-di) - dependency injection with [Koin](https://github.com/InsertKoinIO/koin).
6. [`5-metrics`](https://github.com/mchernyavskaya/ktorwebapp/tree/5-metrics) - added Micrometer metrics to the app.
7. [`6-request-metrics`](https://github.com/mchernyavskaya/ktorwebapp/tree/6-request-metrics) - removed most of the default metrics to have better visibility for the request metrics (don't do that with your real apps!).

## Requirements

- Docker and Docker Compose
- `make` utility to run commands from the `Makefile` 

## Starting the local environment (for branches with db)

```bash
make up
```

## Stopping the local environment (for branches with db)

```bash
make down
```

Have fun with it!  

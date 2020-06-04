package com.example

import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.MySQLContainerProvider

abstract class AbstractContainerBaseTest {

    var mySQLContainer: MySQLContainer<*>? = null

    init {
        mySQLContainer = MySQLContainerProvider().newInstance() as MySQLContainer<*>
        mySQLContainer.start()


    }
}

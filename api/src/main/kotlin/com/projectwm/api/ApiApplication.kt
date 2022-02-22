package com.projectwm.api

import com.projectwm.api.databases.DatabaseConnection
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiApplication

fun main(args: Array<String>) {
	DatabaseConnection().connectDatabase()
	runApplication<ApiApplication>(*args)
}

package com.projectwm.api.databases

import org.jetbrains.exposed.sql.Database
import java.net.URI
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseConnection {

    companion object{
        //        Configuracion Heroku postgres
//        private val dbUri = URI(System.getenv("DATABASE_URL"))
//        val dbUser: String = dbUri.userInfo.split(":")[0]
//        val dbPass: String = dbUri.userInfo.split(":")[1]
//        val dbUrl = "jdbc:postgresql://" + dbUri.host + ':' + dbUri.port + dbUri.path

        //      Configuracion Postgres
        private const val dbUrl = "jdbc:postgresql://localhost:5432/wm"
        private const val dbUser = "postgres"
        private const val dbPass = "sa"
    }

    fun connectDatabase() =
        Database.connect(dbUrl, driver = "org.postgresql.Driver", user = dbUser, password = dbPass)

    @Throws(SQLException::class)
    fun connect(): Connection? = DriverManager.getConnection(dbUrl, dbUser, dbPass)
}

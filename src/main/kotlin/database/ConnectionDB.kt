package org.example.database

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object ConnectionDB {

    private val databaseType = "mysql"

    // Comnfiguración para SQLite
    private val dbPath = "datos/videogames.sqlite"
    private val dbFile = File(dbPath)
    private val sqliteUrl = "jdbc:sqlite:${dbFile.absolutePath}"

    // Configuración para MySQL
    private val mysqlUrl = "jdbc:mysql://localhost:3306/gamelibrary"
    private val mysqlUser = "root"

    fun getConnection(): Connection? {
        return try {
            when (databaseType) {
                "sqlite" -> DriverManager.getConnection(sqliteUrl)
                "mysql" -> {
                    val password = System.getenv("MYSQL_PASSWORD")

                    if (password.isNullOrEmpty()) {
                        println("MYSQL_PASSWORD is not configured")
                        null
                    } else {
                        DriverManager.getConnection(mysqlUrl, mysqlUser, password)
                    }
                }
                else -> {
                    println("Invalid database type")
                    null
                }
            }
        } catch (e: SQLException) {
            println("Error connecting to the database: ${e.message}")
            null
        }
    }

    fun testConnection(): Boolean {
        return getConnection()?.use { conn ->
            val location = if (databaseType == "sqlite") {
                dbFile.absolutePath
            } else {
                mysqlUrl
            }

            println("Connection established successfully to $location")
            true
        } ?: false
    }

    fun closeConnection(conn: Connection?) {
        try {
            if (conn != null && !conn.isClosed) {
                conn.close()
                println("Connection closed correctly")
            }
        } catch (e: SQLException) {
            println("Error closing the database connection: ${e.message}")
        }
    }
}

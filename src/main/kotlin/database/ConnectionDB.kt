package org.example.database

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object ConnectionDB {
    private val dbPath = "datos/videogames.sqlite"
    private val dbFile = File(dbPath)
    private val url = "jdbc:sqlite:${dbFile.absolutePath}"

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(url)
        } catch (e: SQLException) {
            println("Error connecting to the database: ${e.message}")
            null
        }
    }

    fun testConnection(): Boolean {
        return getConnection()?.use { conn ->
            println("Connection established succesfully to ${dbFile.absolutePath}")
            true
        } ?: false
    }

    fun closeConnection(conn: Connection?) {
        try {
            conn?.close()
            println("Connection closed correctly")
        } catch (e: SQLException) {
            println("Error closing the database connection: ${e.message}")
        }
    }
}

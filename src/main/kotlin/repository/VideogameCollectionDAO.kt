package org.example.repository

import org.example.database.ConnectionDB
import kotlin.use

object VideogameCollectionDAO {
    fun addVideogameToCollection(idCollection: Int, idVideogame: Int, dateAdded: String) {
        val sqlSelect = "SELECT id_videogame FROM videogames WHERE id_videogame = ?"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sqlSelect).use { stmt ->
                stmt.setInt(1, idVideogame)

                val rs = stmt.executeQuery()

                if (!rs.next()) {
                    throw Exception(
                        "No videogame found with ID $idVideogame to add to collection."
                    )
                }
            }

            val sqlInsert = """
                INSERT INTO collection_videogames
                (id_collection, id_videogame, date_added)
                VALUES (?, ?, ?)
            """.trimIndent()

            conn.prepareStatement(sqlInsert).use { stmt ->
                stmt.setInt(1, idCollection)
                stmt.setInt(2, idVideogame)
                stmt.setString(3, dateAdded)

                if (stmt.executeUpdate() > 0) {
                    println(
                        "Videogame with ID $idVideogame added to collection $idCollection."
                    )
                }
            }
        }

        println()
    }

    fun removeVideogameInCollection(idCollection: Int, idVideogame: Int) {
        val sql = "DELETE FROM collection_videogames WHERE id_collection = ? AND id_videogame = ?"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, idCollection)
                stmt.setInt(2, idVideogame)

                if (stmt.executeUpdate() > 0) {
                    println("Videogame with ID $idVideogame removed from collection $idCollection successfully.")
                } else {
                    println("Videogame with ID $idVideogame not found in collection $idCollection.")
                }
            }
        }

        println()
    }
}
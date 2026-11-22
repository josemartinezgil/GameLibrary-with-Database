package org.example.repository

import org.example.database.ConnectionDB
import org.example.model.Videogame
import java.sql.SQLException
import kotlin.use

object VideogamesCollectionDAO {

    fun addVideogameToCollection(idCollection: Int, idVideogame: Int, dateAdded: String) {
        val sqlSelectVideogame = "SELECT id_videogame FROM videogames WHERE id_videogame = ?"
        val sqlSelectCollection = "SELECT id_collection FROM collections WHERE id_collection = ?"
        val sqlInsert = "INSERT INTO videogame_collection (id_collection, id_videogame, date_added) VALUES (?, ?, ?)"

        ConnectionDB.getConnection()?.use { conn ->
            try {
                conn.autoCommit = false

                // Comprobamos que el videojuego existe
                conn.prepareStatement(sqlSelectVideogame).use { stmt ->
                    stmt.setInt(1, idVideogame)

                    stmt.executeQuery().use { rs ->
                        if (!rs.next()) {
                            throw SQLException("Videogame with ID $idVideogame not found.")
                        }
                    }
                }

                // Comrpobamos que existe también la colección
                conn.prepareStatement(sqlSelectCollection).use { stmt ->
                    stmt.setInt(1, idCollection)

                    stmt.executeQuery().use { rs ->
                        if (!rs.next()) {
                            throw SQLException("Collection with ID $idCollection not found.")
                        }
                    }
                }

                // Añade el videojuego finalmente a la colección
                conn.prepareStatement(sqlInsert).use { stmt ->
                    stmt.setInt(1, idCollection)
                    stmt.setInt(2, idVideogame)
                    stmt.setString(3, dateAdded)

                    stmt.executeUpdate()
                }

                conn.commit()
                println("Videogame with ID $idVideogame added to collection $idCollection successfully.")

            } catch (e: SQLException) {
                conn.rollback()
                println("Error adding videogame to collection: ${e.message}")
            }
        }

        println()
    }


    fun removeVideogameInCollection(idCollection: Int, idVideogame: Int) {
        val sql = "DELETE FROM videogame_collection WHERE id_collection = ? AND id_videogame = ?"


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


    fun readVideogamesByCollection(idCollection: Int): List<Videogame> {
        val videogames = mutableListOf<Videogame>()

        val sql = """
        SELECT v.id_videogame, v.title, v.genre, v.year, v.rating
        FROM videogames v
        INNER JOIN videogame_collection vc
        ON v.id_videogame = vc.id_videogame
        WHERE vc.id_collection = ?
        ORDER BY v.title
    """.trimIndent()

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, idCollection)

                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        videogames.add(
                            Videogame(
                                idVideogame = rs.getInt("id_videogame"),
                                title = rs.getString("title"),
                                genre = rs.getString("genre"),
                                year = rs.getInt("year"),
                                rating = rs.getDouble("rating")
                            )
                        )
                    }
                }
            }
        }

        return videogames
    }

}
package org.example.repository

import org.example.database.ConnectionDB
import org.example.model.Videogame

object VideogameDAO {
    fun readVideogames(): List<Videogame> {
        val list = mutableListOf<Videogame>()
        val sql = "SELECT * FROM videogames"
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    list.add(Videogame(
                        idVideogame = rs.getInt("id_videogame"),
                        title = rs.getString("title"),
                        genre = rs.getString("genre"),
                        year = rs.getInt("year"),
                        rating = rs.getDouble("rating")
                    ))
                }
            }
        }

        return list
    }

    fun listVideogamesByTitle() {
        println("*** VIDEOGAMES COLLECTION ***")
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement("SELECT * FROM videogames").use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    println("* [${rs.getString("id_videogame")}] ${rs.getString("title")}, ${rs.getInt("year")} (${rs.getString("genre")}) - ${rs.getDouble("rating")}")
                }
            }
        }

        println()
    }

    fun readVideogameById(id: Int): Videogame? {
        var videogame: Videogame? = null
        val sql = "SELECT * FROM videogames WHERE id_videogame = ?"
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    videogame = Videogame(
                        idVideogame = rs.getInt("id_videogame"),
                        title = rs.getString("title"),
                        genre = rs.getString("genre"),
                        year = rs.getInt("year"),
                        rating = rs.getDouble("rating")
                    )
                }
            }
        }

        return videogame
    }

    fun listVideogameById(videogame: Videogame) {
        println("*** VIDEOGAME ***")
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement("SELECT * FROM videogames WHERE id_videogame = ${videogame.idVideogame}").use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    println("* [${rs.getString("id_videogame")}] ${rs.getString("title")}, ${rs.getInt("year")} (${rs.getString("genre")}) - ${rs.getInt("rating")}")
                }
            }
        }

        println()
    }

    fun insertVideogame(videogame: Videogame) {
        val sql = "INSERT INTO videogames(title, genre, year, rating) VALUES (?, ?, ?, ?)"
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, videogame.title)
                stmt.setString(2, videogame.genre)
                stmt.setInt(3, videogame.year)
                stmt.setDouble(4, videogame.rating)
                if (stmt.executeUpdate() > 0) {
                    println("${videogame.title} inserted succesfully.")
                }
            }
        }

        println()
    }

    fun updateVideogame(videogame: Videogame) {
        if (videogame.idVideogame == null) {
            println("Error: Cannot update any videogame without an ID.")
            return
        }
        val sql = "UPDATE videogames SET title = ?, genre = ?, year = ?, rating = ? WHERE id_videogame = ?"
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, videogame.title)
                stmt.setString(2, videogame.genre)
                stmt.setInt(3, videogame.year)
                stmt.setDouble(4, videogame.rating)
                stmt.setInt(5, videogame.idVideogame)
                if (stmt.executeUpdate() > 0) {
                    println("Videogame with ID ${videogame.idVideogame} updated succesfully.")
                } else {
                    println("No videogame found with ID ${videogame.idVideogame} to update.")
                }
            }
        }

        println()
    }

    fun updateRatingVideogame(videogame: Videogame, newRating: Double) {
        if (videogame.idVideogame == null) {
            println("Error: Cannot update any videogame without an ID.")
            return
        }
        val sql = "UPDATE videogames SET rating = ? WHERE id_videogame = ?"
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setDouble(1, newRating)
                stmt.setInt(2, videogame.idVideogame)
                if (stmt.executeUpdate() > 0) {
                    println("Videogame with ID ${videogame.idVideogame} updated succesfully.")
                } else {
                    println("No videogame found with ID ${videogame.idVideogame} to update.")
                }
            }
        }

        println()
    }

    fun deleteVideogame(id: Int) {
        val sql = "DELETE FROM videogames WHERE id_videogame = ?"
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                if (stmt.executeUpdate() > 0) {
                    println("Videogame with ID $id deleted succesfully.")
                } else {
                    println("No videogame found with ID $id to delete.")
                }
            }
        }

        println()
    }

    // Implementada en VideogameCollection
    fun addVideogameToCollection(idCollection: Int, idVideogame: Int, date: String) {
        val sqlSelect = "SELECT id_videogame FROM videogames WHERE id = ?"
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sqlSelect).use { stmt ->
                stmt.setInt(1, idVideogame)

                val rs = stmt.executeQuery()

                if (!rs.next()) {
                    throw Exception("No videogame found with ID $idVideogame to collection.")
                }

                rs.close()
                stmt.close()

                val sqlInsert = "INSERT INTO collection_videogames (id_collection, id_videogame, date_added) VALUES (?, ?, ?)"

                val insert = conn.prepareStatement(sqlInsert)

                insert.setInt(1, idCollection)
                insert.setInt(2, idVideogame)
                insert.setString(3, date)

                insert.executeUpdate()
                insert.close()
            }
        }
    }
}
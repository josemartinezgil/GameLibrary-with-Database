package org.example.repository

import org.example.database.ConnectionDB
import org.example.model.CollectionGame

object CollectionGameDAO {

    fun readCollections(): List<CollectionGame> {
        val list = mutableListOf<CollectionGame>()
        val sql = "SELECT * FROM collections"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()

                while (rs.next()) {
                    list.add(
                        CollectionGame(
                            idCollection = rs.getInt("id_collection"),
                            name = rs.getString("name"),
                            idPlayer = rs.getInt("id_player")
                        )
                    )
                }
            }
        }

        return list
    }

    fun listCollectionsByName() {
        println("*** COLLECTIONS ***")

        val sql = "SELECT * FROM collections ORDER BY name"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()

                while (rs.next()) {
                    println(
                        "* [${rs.getInt("id_collection")}] ${rs.getString("name")}"
                    )
                }
            }
        }

        println()
    }

    fun readCollectionById(id: Int): CollectionGame? {
        var collectionGame: CollectionGame? = null
        val sql = "SELECT * FROM collections WHERE id_collection = ?"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)

                val rs = stmt.executeQuery()

                if (rs.next()) {
                    collectionGame = CollectionGame(
                        idCollection = rs.getInt("id_collection"),
                        name = rs.getString("name"),
                        idPlayer = rs.getInt("id_player")
                    )
                }
            }
        }

        return collectionGame
    }

    fun listCollectionById(collectionGame: CollectionGame) {
        println("\n*** COLLECTION ***")

        val sql = "SELECT * FROM collections WHERE id_collection = ?"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, collectionGame.idCollection)

                val rs = stmt.executeQuery()

                while (rs.next()) {
                    println(
                        "* [${rs.getInt("id_collection")}] ${rs.getString("name")}"
                    )
                }
            }
        }

        println()
    }

    fun insertCollection(collectionGame: CollectionGame) {
        val sql = "INSERT INTO collections(name, id_player) VALUES (?, ?)"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, collectionGame.name)
                stmt.setInt(2, collectionGame.idPlayer)

                if (stmt.executeUpdate() > 0) {
                    println("${collectionGame.name} inserted successfully.")
                }
            }
        }
        println()
    }

    fun updateCollectionName(collectionGame: CollectionGame, newName: String) {
        val sql = "UPDATE collections SET name = ? WHERE id_collection = ?"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, newName)
                stmt.setInt(2, collectionGame.idCollection)

                if (stmt.executeUpdate() > 0) {
                    println(
                        "Collection with ID ${collectionGame.idCollection} updated successfully."
                    )
                } else {
                    println(
                        "No collection found with ID ${collectionGame.idCollection} to update."
                    )
                }
            }
        }

        println()
    }

    fun updateNameCollection(collectionGame: CollectionGame, newName: String) {
        val sql = "UPDATE collections SET name = ? WHERE id_collection = ?"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, newName)
                stmt.setInt(2, collectionGame.idCollection)

                if (stmt.executeUpdate() > 0) {
                    println(
                        "Collection with ID ${collectionGame.idCollection} updated successfully."
                    )
                } else {
                    println(
                        "No collection found with ID ${collectionGame.idCollection} to update."
                    )
                }
            }
        }

        println()
    }

    fun deleteCollection(id: Int) {
        val sql = "DELETE FROM collections WHERE id_collection = ?"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)

                if (stmt.executeUpdate() > 0) {
                    println("Collection with ID $id deleted successfully.")
                } else {
                    println("No collection found with ID $id to delete.")
                }
            }
        }

        println()
    }




}

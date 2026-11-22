package org.example.repository

import org.example.database.ConnectionDB
import org.example.model.Player
import kotlin.use

object PlayerDAO {
    fun readPlayerById(id: Int): Player? {
        var player: Player? = null

        val sql = "SELECT id_player, name FROM players WHERE id_player = ?"

        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)

                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        player = Player(
                            idPlayer = rs.getInt("id_player"),
                            name = rs.getString("name")
                        )
                    }
                }
            }
        }

        return player
    }

    fun insertPlayer(player: Player) {
        val sql = "INSERT INTO players(name) VALUES (?)"
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, player.name)
                if (stmt.executeUpdate() > 0) {
                    println("${player.name} inserted successfully.")
                }
            }
        }

        println()
    }

    fun deletePlayer(id: Int) {
        val sql = "DELETE FROM players WHERE id_player = ?"
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                if (stmt.executeUpdate() > 0) {
                    println("Player with ID $id deleted successfully.")
                } else {
                    println("No player found with ID $id to delete.")
                }
            }
        }

        println()
    }
}
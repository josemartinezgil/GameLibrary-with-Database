package org.example.repository

import org.example.database.ConnectionDB
import org.example.model.Player
import kotlin.use

object PlayerDAO {
    fun insertPlayer(player: Player) {
        val sql = "INSERT INTO players(name) VALUES (?)"
        ConnectionDB.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, player.name)
                if (stmt.executeUpdate() > 0) {
                    println("${player.name} inserted succesfully.")
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
                    println("Player with ID $id deleted succesfully.")
                } else {
                    println("No player found with ID $id to delete.")
                }
            }
        }

        println()
    }
}
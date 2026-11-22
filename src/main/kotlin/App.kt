package org.example

import org.example.database.ConnectionDB
import org.example.model.CollectionGame
import org.example.model.Player
import org.example.model.Videogame
import org.example.repository.CollectionGameDAO
import org.example.repository.PlayerDAO
import org.example.repository.VideogamesCollectionDAO
import org.example.repository.VideogameDAO

val DATABASE = ConnectionDB.getConnection() ?: throw Exception("Database connection failed")
val VIDEOGAME_DAO = VideogameDAO
val COLLECTION_DAO = CollectionGameDAO
val VIDEOGAME_COLLECTION_DAO = VideogamesCollectionDAO
val PLAYER_DAO = PlayerDAO

val newVideogame = Videogame(
    6,
    "Elden Ring",
    "Souls-like",
    2022,
    9.6
)

val newCollection = CollectionGame(
    4,
    "Playstation",
    1
)

val newPlayer = Player(
    1,
    "Jose"
)

fun main() {

    // Testing
    if (!ConnectionDB.testConnection()) {
        println("Unable to connect to the database")
        return
    }

    /*
    ConnectionDB.testConnection()

    principalMenu()

     */

    /*
    try {
        database.autoCommit = false

        // lógica

        database.commit()
        println("The operations has been executed correctly")
    } catch (e: Exception) {
        database.rollback()
        println("The operations has not been executed")
        println("Error: ${e.message}")
    }
    */

    ConnectionDB.closeConnection(DATABASE)
}

fun principalMenuView() {
    println("\n===================\n" +
            "📚 GAME LIBRARY 📚\n" +
            "===================\n" +
            "1. Videogame Menu.\n" +
            "2. Player Menu.\n" +
            "0. End Session.")
}

fun principalMenu() {
    var option: Int
    do {
        principalMenuView()
        option = selectOptionMenu()

        when (option) {
            1 -> videogameMenu()
            2 -> playerMenu()
            0 -> println("Session is ending...")
            else -> println("Invalid option. Try again.")
        }

    } while (option != 0)
}


fun videogameMenuView() {
    println("\n====================\n" +
            "🕹️ VIDEOGAME MENU 🕹️\n" +
            "====================\n" +
            "1. List all videogames.\n" +
            "2. Add new videogame.\n" +
            "3. List videogame by ID.\n" +
            "4. Update videogame rating.\n" +
            "5. Remove videogame.\n" +
            "0. Return to Game Library Menu.\n")
}

fun videogameMenu() {
    var option: Int
    do {
        videogameMenuView()
        option = selectOptionMenu()

        when (option) {
            1 -> VIDEOGAME_DAO.listVideogamesByTitle()
            2 -> VIDEOGAME_DAO.insertVideogame(newVideogame)
            3 -> VIDEOGAME_DAO.listVideogameById(newVideogame)
            4 -> VIDEOGAME_DAO.updateRatingVideogame(newVideogame, 9.6)
            5 -> VIDEOGAME_DAO.deleteVideogame(6)
            0 -> println("Returning to game library menu...")
            else -> println("Invalid option. Try again.")
        }

    } while (option != 0)
}

fun collectionMenuView(idPlayer: Int) {
    println("\n=====================\n" +
            "🗂️ PLAYER $idPlayer COLLECTION MENU ️🗂️\n" +
            "=====================\n" +
            "1. List all collections.\n" +
            "2. Add new collection.\n" +
            "4. Update collection name.\n" +
            "5. Add videogame to collection.\n" +
            "6. Remove videogame to collection.\n" +
            "7. Remove collection.\n" +
            "0. Return to Player Menu.\n")
}

fun collectionMenu(idPlayer: Int) {
    var option: Int
    do {
        collectionMenuView(idPlayer)
        option = selectOptionMenu()

        when (option) {
            1 -> COLLECTION_DAO.listCollectionsByName()
            2 -> COLLECTION_DAO.insertCollection(newCollection)
            3 -> COLLECTION_DAO.listCollectionById(newCollection)
            4 -> COLLECTION_DAO.updateCollectionName(newCollection, "XBOX")
            5 -> VIDEOGAME_COLLECTION_DAO.addVideogameToCollection(1,6,"09-11-2026")
            6 -> VIDEOGAME_COLLECTION_DAO.removeVideogameInCollection(1,6)
            7 -> COLLECTION_DAO.deleteCollection(7)
            0 -> println("Returning to player menu...")
            else -> println("Invalid option. Try again.")
        }

    } while (option != 0)
}

fun playerMenuView() {
    println("\n=================\n" +
            "👽 PLAYER MENU 👽\n" +
            "=================\n" +
            "1. Create new player.\n" +
            "2. Remove new player.\n" +
            "3. Edit player's collections.\n" +
            "0. Return to Game Library Menu.\n")
}

fun playerMenu() {
    var option: Int

    do {
        playerMenuView()
        option = selectOptionMenu()

        when (option) {
            1 -> PLAYER_DAO.insertPlayer(newPlayer)
            2 -> PLAYER_DAO.deletePlayer(1)
            3 -> loginPlayer()
            0 -> println("Returning to game library menu...")
            else -> println("Invalid option. Try again.")
        }

    } while (option != 0)
}

fun loginPlayer() {
    print("Insert Player ID: ")
    val idPlayer = readln().toInt()

    collectionMenu(idPlayer)
}

fun selectOptionMenu(): Int {
    println()
    print("Select an option: ")
    return readln().toIntOrNull() ?: -1
}
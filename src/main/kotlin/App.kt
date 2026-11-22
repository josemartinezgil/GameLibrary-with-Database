package org.example

import org.example.database.ConnectionDB
import org.example.repository.CollectionGameDAO
import org.example.repository.PlayerDAO
import org.example.repository.VideogamesCollectionDAO
import org.example.repository.VideogameDAO
import org.example.service.AppService

val VIDEOGAME_DAO = VideogameDAO
val COLLECTION_DAO = CollectionGameDAO
val VIDEOGAME_COLLECTION_DAO = VideogamesCollectionDAO
val PLAYER_DAO = PlayerDAO
val SERVICE = AppService

fun main() {

    if (!ConnectionDB.testConnection()) {
        println("Unable to connect to the database")
        return
    }

    principalMenu()

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
            2 -> SERVICE.addNewVideogame()
            3 -> SERVICE.showVideogameById()
            4 -> SERVICE.updateVideogameRating()
            5 -> SERVICE.removeVideogame()
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
            "3. List collection by ID.\n" +
            "4. Update collection name.\n" +
            "5. Add videogame to collection.\n" +
            "6. Remove videogame from collection.\n" +
            "7. Remove collection.\n" +
            "0. Return to Player Menu.\n")
}

fun collectionMenu(idPlayer: Int) {
    var option: Int
    do {
        collectionMenuView(idPlayer)
        option = selectOptionMenu()


        when (option) {
            1 -> SERVICE.showPlayerCollections(idPlayer)
            2 -> SERVICE.addNewCollection(idPlayer)
            3 -> SERVICE.showCollectionById()
            4 -> SERVICE.updateCollectionName()
            5 -> SERVICE.addVideogameToCollection()
            6 -> SERVICE.removeVideogameFromCollection()
            7 -> SERVICE.removeCollection()
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
            "2. Remove player.\n" +
            "3. Edit player's collections.\n" +
            "0. Return to Game Library Menu.\n")
}

fun playerMenu() {
    var option: Int

    do {
        playerMenuView()
        option = selectOptionMenu()

        when (option) {
            1 -> SERVICE.addNewPlayer()
            2 -> SERVICE.removePlayer()
            3 -> loginPlayer()
            0 -> println("Returning to game library menu...")
            else -> println("Invalid option. Try again.")
        }

    } while (option != 0)
}

fun loginPlayer() {
    print("Insert Player ID: ")
    val idPlayer = readln().toIntOrNull()

    if (idPlayer == null || idPlayer <= 0) {
        println("Invalid player ID.")
        return
    }

    val player = PLAYER_DAO.readPlayerById(idPlayer)

    if (player == null) {
        println("Player not found.")
        return
    }

    println("Welcome, ${player.name}!")
    collectionMenu(idPlayer)
}

fun selectOptionMenu(): Int {
    println()
    print("Select an option: ")
    return readln().toIntOrNull() ?: -1
}

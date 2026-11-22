package org.example.service

import org.example.repository.CollectionGameDAO
import org.example.repository.PlayerDAO
import org.example.repository.VideogamesCollectionDAO
import org.example.repository.VideogameDAO
import org.example.model.CollectionGame
import org.example.model.Player
import org.example.model.Videogame

object AppService {

    fun addNewVideogame() {
        print("Insert videogame title: ")
        val title = readln()

        print("Insert videogame genre: ")
        val genre = readln()

        print("Insert videogame year: ")
        val year = readln().toIntOrNull()

        if (year == null) {
            println("Invalid year.")
            return
        }

        print("Insert videogame rating: ")
        val rating = readln().toDoubleOrNull()

        if (rating == null) {
            println("Invalid rating.")
            return
        }

        val videogame = Videogame(
            idVideogame = 0,
            title = title,
            genre = genre,
            year = year,
            rating = rating
        )

        VideogameDAO.insertVideogame(videogame)
    }

    fun showVideogameById() {
        print("Insert videogame ID: ")
        val id = readln().toIntOrNull()

        if (id == null) {
            println("Invalid videogame ID.")
            return
        }

        val videogame = VideogameDAO.readVideogameById(id)

        if (videogame != null) {
            VideogameDAO.listVideogameById(videogame)
        } else {
            println("Videogame not found.")
        }
    }


    fun updateVideogameRating() {
        print("Insert videogame ID: ")
        val id = readln().toIntOrNull()

        if (id == null) {
            println("Invalid videogame ID.")
            return
        }

        val videogame = VideogameDAO.readVideogameById(id)

        if (videogame == null) {
            println("Videogame not found.")
            return
        }

        print("Insert new rating: ")
        val newRating = readln().toDoubleOrNull()

        if (newRating == null || newRating < 0 || newRating > 10) {
            println("Invalid rating. Enter a value between 0 and 10.")
            return
        }

        VideogameDAO.updateRatingVideogame(videogame, newRating)
    }


    fun removeVideogame() {
        print("Insert videogame ID: ")
        val id = readln().toIntOrNull()

        if (id == null) {
            println("Invalid videogame ID.")
            return
        }

        val videogame = VideogameDAO.readVideogameById(id)

        if (videogame == null) {
            println("Videogame not found.")
            return
        }

        VideogameDAO.deleteVideogame(id)
    }


    fun addNewPlayer() {
        print("Insert player name: ")
        val name = readln().trim()

        if (name.isEmpty()) {
            println("Invalid player name.")
            return
        }

        val player = Player(
            idPlayer = 0,
            name = name
        )

        PlayerDAO.insertPlayer(player)
    }


    fun removePlayer() {
        print("Insert player ID: ")
        val id = readln().toIntOrNull()

        if (id == null || id <= 0) {
            println("Invalid player ID.")
            return
        }

        PlayerDAO.deletePlayer(id)
    }


    fun showPlayerCollections(idPlayer: Int) {
        val collections = CollectionGameDAO.readCollections()
        var found = false

        for (collection in collections) {
            if (collection.idPlayer == idPlayer) {
                CollectionGameDAO.listCollectionById(collection)
                found = true
            }
        }

        if (!found) {
            println("This player has no collections.")
        }
    }


    fun addNewCollection(idPlayer: Int) {
        print("Insert collection name: ")
        val name = readln().trim()

        if (name.isEmpty()) {
            println("Invalid collection name.")
            return
        }

        val collection = CollectionGame(
            idCollection = 0,
            name = name,
            idPlayer = idPlayer
        )

        CollectionGameDAO.insertCollection(collection)
    }



    fun showCollectionById(idPlayer: Int) {
        print("Insert collection ID: ")
        val id = readln().toIntOrNull()

        if (id == null || id <= 0) {
            println("Invalid collection ID.")
            return
        }

        val collection = CollectionGameDAO.readCollectionById(id)

        if (collection != null && collection.idPlayer == idPlayer) {
            CollectionGameDAO.listCollectionById(collection)

            val videogames = VideogamesCollectionDAO.readVideogamesByCollection(id)

            if (videogames.isEmpty()) {
                println("This collection has no videogames.")
            } else {
                println("Videogames in this collection:")

                for (videogame in videogames) {
                    println("${videogame.idVideogame}. ${videogame.title} - Rating: ${videogame.rating}")
                }
            }
        } else {
            println("Collection not found or does not belong to this player.")
        }
    }



    fun updateCollectionName(idPlayer: Int) {
        print("Insert collection ID: ")
        val id = readln().toIntOrNull()

        if (id == null || id <= 0) {
            println("Invalid collection ID.")
            return
        }

        val collection = CollectionGameDAO.readCollectionById(id)

        if (collection == null || collection.idPlayer != idPlayer) {
            println("Collection not found or does not belong to this player.")
            return
        }

        print("Insert new collection name: ")
        val newName = readln().trim()

        if (newName.isEmpty()) {
            println("Invalid collection name.")
            return
        }

        CollectionGameDAO.updateCollectionName(collection, newName)
    }


    fun removeCollection(idPlayer: Int) {
        print("Insert collection ID: ")
        val id = readln().toIntOrNull()

        if (id == null || id <= 0) {
            println("Invalid collection ID.")
            return
        }

        val collection = CollectionGameDAO.readCollectionById(id)

        if (collection == null || collection.idPlayer != idPlayer) {
            println("Collection not found or does not belong to this player.")
            return
        }

        CollectionGameDAO.deleteCollection(id)
    }


    fun addVideogameToCollection(idPlayer: Int) {
        print("Insert collection ID: ")
        val idCollection = readln().toIntOrNull()

        print("Insert videogame ID: ")
        val idVideogame = readln().toIntOrNull()

        if (idCollection == null || idCollection <= 0 ||
            idVideogame == null || idVideogame <= 0) {
            println("Invalid ID.")
            return
        }

        val collection = CollectionGameDAO.readCollectionById(idCollection)

        if (collection == null || collection.idPlayer != idPlayer) {
            println("Collection not found or does not belong to this player.")
            return
        }

        print("Insert date (YYYY-MM-DD hh:mm:ss): ")
        val dateAdded = readln()

        VideogamesCollectionDAO.addVideogameToCollection(
            idCollection,
            idVideogame,
            dateAdded
        )
    }


    fun removeVideogameFromCollection(idPlayer: Int) {
        print("Insert collection ID: ")
        val idCollection = readln().toIntOrNull()

        print("Insert videogame ID: ")
        val idVideogame = readln().toIntOrNull()

        if (idCollection == null || idCollection <= 0 ||
            idVideogame == null || idVideogame <= 0) {
            println("Invalid ID.")
            return
        }

        val collection = CollectionGameDAO.readCollectionById(idCollection)

        if (collection == null || collection.idPlayer != idPlayer) {
            println("Collection not found or does not belong to this player.")
            return
        }

        VideogamesCollectionDAO.removeVideogameInCollection(
            idCollection,
            idVideogame
        )
    }
}
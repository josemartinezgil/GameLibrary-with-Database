package org.example.model

data class Player(
    val idPlayer: Int,
    val name: String,
    val collections: MutableList<CollectionGame> ?= null
)

package com.xlebnick.kitties.data.remote.model

data class KittyRemoteModel(
    val url: String,
    val id: String,
    val breeds: List<BreedRemoteModel> = listOf()
)

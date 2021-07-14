package com.xlebnick.kitties.data.remote.model

data class LikeRemoteModel(
    val id: String,
    val userId: String,
    val subId: String,
    val imageId: String,
    val image: KittyRemoteModel
)
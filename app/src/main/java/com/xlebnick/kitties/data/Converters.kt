package com.xlebnick.kitties.data

import com.xlebnick.kitties.data.model.Breed
import com.xlebnick.kitties.data.model.Kitty
import com.xlebnick.kitties.data.model.Like
import com.xlebnick.kitties.data.remote.model.BreedRemoteModel
import com.xlebnick.kitties.data.remote.model.KittyRemoteModel
import com.xlebnick.kitties.data.remote.model.LikeRemoteModel

fun KittyRemoteModel.asKitty(isLiked: Boolean) =
    Kitty(url, id, breeds.map { it.asBreed() }, isLiked)

fun BreedRemoteModel.asBreed() = Breed(id, name)
fun LikeRemoteModel.asLike() = Like(id, imageId)
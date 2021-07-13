package com.xlebnick.kitties.data.remote

import com.xlebnick.kitties.data.model.Breed
import com.xlebnick.kitties.data.model.Kitty
import com.xlebnick.kitties.data.remote.model.BreedRemoteModel
import com.xlebnick.kitties.data.remote.model.KittyRemoteModel
import com.xlebnick.kitties.data.remote.model.LikeKittyArg
import com.xlebnick.kitties.data.remote.model.LikeRemoteModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val api: Api) {
    suspend fun fetchKitties(page: Int, limit: Int, breeds: List<Breed>?): List<KittyRemoteModel> =
        api.getKitties(page, limit, breeds?.joinToString(separator = ",") { it.id })

    suspend fun fetchLiked(): List<LikeRemoteModel> =
        api.getLikedKitties("some user")

    suspend fun likeKitty(kitty: Kitty) = api.likeKitty(LikeKittyArg(kitty.id, "some user"))

    suspend fun fetchBreeds(): List<BreedRemoteModel> =
        api.getBreeds()
}

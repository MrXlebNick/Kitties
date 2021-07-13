package com.xlebnick.kitties.data.remote

import com.xlebnick.kitties.data.remote.model.BreedRemoteModel
import com.xlebnick.kitties.data.remote.model.KittyRemoteModel
import com.xlebnick.kitties.data.remote.model.LikeKittyArg
import com.xlebnick.kitties.data.remote.model.LikeRemoteModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("images/search")
    suspend fun getKitties(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("breed_ids") breedIds: String?,
        @Query("order") order: String = "ASC"
    ): List<KittyRemoteModel>

    @POST("favourites")
    suspend fun likeKitty(@Body likeKittyArg: LikeKittyArg)

    @GET("favourites")
    suspend fun getLikedKitties(@Query("sub_id") subId: String): List<LikeRemoteModel>

    @DELETE("favourites/{kittyId}")
    suspend fun unlikeKitty(@Path("kittyId") kittyId: String)

    @GET("breeds")
    suspend fun getBreeds(): List<BreedRemoteModel>
}
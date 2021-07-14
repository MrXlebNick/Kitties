package com.xlebnick.kitties.data.remote

import com.xlebnick.kitties.data.remote.model.BreedRemoteModel
import com.xlebnick.kitties.data.remote.model.KittyRemoteModel
import com.xlebnick.kitties.data.remote.model.LikeKittyArg
import com.xlebnick.kitties.data.remote.model.LikeRemoteModel
import com.xlebnick.kitties.data.remote.model.LikeResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("images/search")
    suspend fun getKitties(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("breed_ids") breedIds: String?,
        @Query("order") order: String = "ASC",
        @Query("include_breeds") includeBreeds: Boolean = true
    ): List<KittyRemoteModel>

    @POST("favourites")
    suspend fun likeKitty(@Body likeKittyArg: LikeKittyArg): LikeResponse

    @GET("favourites")
    suspend fun getLikedKitties(@Query("sub_id") subId: String): List<LikeRemoteModel>

    @DELETE("favourites/{kittyId}")
    suspend fun unlikeKitty(
        @Path("kittyId") kittyId: String,
        @Query("sub_id") subId: String
    ): LikeResponse

    @GET("breeds")
    suspend fun getBreeds(): List<BreedRemoteModel>

    @Multipart
    @POST("images/upload")
    suspend fun upload(@Part file: MultipartBody.Part)
}
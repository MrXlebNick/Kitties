package com.xlebnick.kitties.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kitty(
    val url: String,
    val id: String,
    val breeds: List<Breed> = listOf(),
    var isLiked: Boolean
) : Parcelable

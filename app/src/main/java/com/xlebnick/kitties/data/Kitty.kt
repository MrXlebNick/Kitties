package com.xlebnick.kitties.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kitty(
    val url: String,
    val id: String,
    val categories: List<String>,
    val breeds: List<String>,
    val isLiked: Boolean
) : Parcelable

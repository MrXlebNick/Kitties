package com.xlebnick.kitties.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Like(val imageId: String, val id: String, val imageUrl: String) : Parcelable
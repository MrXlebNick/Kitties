package com.xlebnick.kitties.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Breed(val id: String, val name: String) : Parcelable
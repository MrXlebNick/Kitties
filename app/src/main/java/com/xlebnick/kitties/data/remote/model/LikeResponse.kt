package com.xlebnick.kitties.data.remote.model

data class LikeResponse(val message: String, val id: String? = null, val status: Int)

const val STATUS_SUCCESS = "SUCCESS"

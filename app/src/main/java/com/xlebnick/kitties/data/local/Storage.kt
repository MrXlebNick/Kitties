package com.xlebnick.kitties.data.local

import android.content.Context
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Storage @Inject constructor(appContext: Context) {

    companion object {
        private const val USER_KEY = "userkey"
    }

    private val sPrefs = appContext.getSharedPreferences("Kitties", Context.MODE_PRIVATE)

    fun getUserId(): String {
        var userKey = sPrefs.getString(USER_KEY, null)
        if (userKey == null) {
            userKey = UUID.randomUUID().toString()
            sPrefs.edit().putString(USER_KEY, userKey).apply()
        }
        return userKey
    }
}

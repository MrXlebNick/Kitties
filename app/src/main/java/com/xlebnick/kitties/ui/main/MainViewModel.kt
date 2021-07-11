package com.xlebnick.kitties.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xlebnick.kitties.data.Kitty
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
    private val _breeds: MutableLiveData<List<String>> =
        MutableLiveData(listOf("Sphynx", "Persian"))
    val breeds: LiveData<List<String>> = _breeds

    fun likeKitty(kitty: Kitty) {
    }

    fun applyFilter(breed: String?) {

    }
}
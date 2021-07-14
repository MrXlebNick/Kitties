package com.xlebnick.kitties.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.xlebnick.kitties.data.asBreed
import com.xlebnick.kitties.data.asKitty
import com.xlebnick.kitties.data.asLike
import com.xlebnick.kitties.data.model.Breed
import com.xlebnick.kitties.data.model.Kitty
import com.xlebnick.kitties.data.model.Like
import com.xlebnick.kitties.data.remote.Repository
import com.xlebnick.kitties.data.remote.model.KittyRemoteModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

const val KITTIES_PAGE_SIZE = 24

class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val breeds: LiveData<List<Breed>> =
        liveData { emit(repository.fetchBreeds().map { it.asBreed() }) }

    private val _kitties = MutableLiveData<List<Kitty>>()
    val kitties: LiveData<List<Kitty>> = _kitties

    private val _likedKitties = MutableLiveData<List<Like>>()
    val likedKitties: LiveData<List<Like>> = _likedKitties

    private var currentKittiesPage = 0
    private var breedFilter: Breed? = null
    private var likedLoadingJob: Job = loadLiked()

    init {
        loadPageOfKitties()
    }

    private fun loadLiked(): Job {
        return viewModelScope.launch {
            _likedKitties.value = repository.fetchLiked().map { it.asLike() }
        }
    }

    fun loadPageOfKitties() {
        viewModelScope.launch {
            var kitties: List<KittyRemoteModel>? = null

            val kittiesLoadingJob = launch {
                val filters = if (breedFilter != null) listOf(breedFilter!!) else null
                try {
                    kitties =
                        repository.fetchKitties(currentKittiesPage, KITTIES_PAGE_SIZE, filters)
                    currentKittiesPage++
                } catch (e: Exception) {
                    // TODO: proper error handling
                    Log.w("***", "failed to fetch another page of kitties", e)
                }
            }

            joinAll(kittiesLoadingJob, likedLoadingJob)
            val existingKitties = _kitties.value?.toMutableList() ?: mutableListOf()
            val newKitties = kitties?.map { kitty ->
                kitty.asKitty(_likedKitties.value?.any { it.imageId == kitty.id } == true)
            } ?: listOf()
            _kitties.value = existingKitties + newKitties

        }
    }

    fun toggleLikeKitty(kitty: Kitty) {
        viewModelScope.launch {
            if (kitty.isLiked) {
                repository.unlikeKitty(kitty.id)
            } else {
                repository.likeKitty(kitty.id)
            }
            val savedKitties = _kitties.value?.toMutableList() ?: return@launch
            savedKitties.map { if (it.id == kitty.id) it.copy(isLiked = !it.isLiked) else it }
            _kitties.value = savedKitties
        }
    }

    fun applyFilter(breed: Breed?) {
        if (breed == breedFilter) return // don't reapply same filter
        breedFilter = breed
        currentKittiesPage = 0
        _kitties.value = listOf()
        loadPageOfKitties()
    }
}
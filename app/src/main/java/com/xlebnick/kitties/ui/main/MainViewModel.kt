package com.xlebnick.kitties.ui.main

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
import com.xlebnick.kitties.data.remote.model.LikeResponse
import com.xlebnick.kitties.data.remote.model.STATUS_SUCCESS
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

const val KITTIES_PAGE_SIZE = 24

class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val breeds: LiveData<List<Breed>> =
        liveData { emit(repository.fetchBreeds().map { it.asBreed() }) }

    private val kittiesList = mutableListOf<Kitty>()
    private val _kitties = MutableLiveData<List<Kitty>>()
    val kitties: LiveData<List<Kitty>> = _kitties

    private val likesList = mutableListOf<Like>()
    private val _likedKitties = MutableLiveData<List<Like>>()
    val likedKitties: LiveData<List<Like>> = _likedKitties

    private val _error = MutableSharedFlow<RequestError>()
    val error: SharedFlow<RequestError> = _error

    private var currentKittiesPage = 0
    private var breedFilter: Breed? = null
    private var likedLoadingJob: Job = loadLiked()

    init {
        loadPageOfKitties()
    }

    private fun loadLiked(): Job {
        return viewModelScope.launch {
            likesList.clear()
            likesList.addAll(repository.fetchLiked().map { it.asLike() })
            _likedKitties.value = likesList
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
                    _error.emit(RequestError.FetchRequestError(e.message ?: "Unknown"))
                }
            }

            joinAll(kittiesLoadingJob, likedLoadingJob)
            val newKitties = kitties?.map { kitty ->
                kitty.asKitty(_likedKitties.value?.any { it.imageId == kitty.id } == true)
            } ?: listOf()
            kittiesList.addAll(newKitties)
            _kitties.value = kittiesList

        }
    }

    fun toggleLikeKitty(kitty: Kitty) {
        viewModelScope.launch {
            if (kitty.isLiked) {
                val likeId = likesList.firstOrNull { it.imageId == kitty.id }?.id
                unlikeKitty(likeId, kitty.id)
            } else {
                likeKitty(kitty.id, kitty.url)
            }
        }
    }

    fun unlikeKitty(like: Like) {
        viewModelScope.launch {
            unlikeKitty(like.id, like.imageId)
        }
    }

    private suspend fun unlikeKitty(likeId: String?, kittyId: String) {
        val response = try {
            repository.unlikeKitty(likeId = likeId!!)
        } catch (e: Throwable) {
            LikeResponse(e.message ?: "Unknown", null, status = 0)
        }
        if (response.message == STATUS_SUCCESS) {
            likesList.remove(likesList.firstOrNull { it.id == likeId })
            _likedKitties.value = likesList

            kittiesList.find { it.id == kittyId }?.run {
                isLiked = false
            }
            _kitties.value = kittiesList
        } else {
            _error.emit(RequestError.UnlikeRequestError(response.message))
        }
    }

    private suspend fun likeKitty(kittyId: String, kittyUrl: String) {
        val response = try {
            repository.likeKitty(kittyId = kittyId)
        } catch (e: Throwable) {
            LikeResponse(e.message ?: "Unknown", null, status = 0)
        }
        if (response.message == STATUS_SUCCESS && response.id != null) {
            likesList.add(Like(kittyId, response.id, kittyUrl))
            _likedKitties.value = likesList
            kittiesList.find { it.id == kittyId }?.run {
                isLiked = true
            }
            _kitties.value = kittiesList
        } else {
            _error.emit(RequestError.LikeRequestError(response.message))
        }
    }


    fun applyFilter(breed: Breed?) {
        if (breed == breedFilter) return // don't reapply same filter
        breedFilter = breed
        currentKittiesPage = 0
        kittiesList.clear()
        _kitties.value = kittiesList
        loadPageOfKitties()
    }
}

sealed class RequestError {
    class LikeRequestError(val message: String) : RequestError()
    class UnlikeRequestError(val message: String) : RequestError()
    class FetchRequestError(val message: String) : RequestError()
}

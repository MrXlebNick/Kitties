package com.xlebnick.kitties.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.xlebnick.kitties.data.asBreed
import com.xlebnick.kitties.data.asKitty
import com.xlebnick.kitties.data.asLike
import com.xlebnick.kitties.data.model.Breed
import com.xlebnick.kitties.data.model.Kitty
import com.xlebnick.kitties.data.model.Like
import com.xlebnick.kitties.data.remote.Repository
import com.xlebnick.kitties.data.remote.model.KittyRemoteModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val KITTIES_PAGE_SIZE = 24

class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val breeds: LiveData<List<Breed>> =
        liveData {
            emit(repository.fetchBreeds().map { it.asBreed() })
        }

    private val _likedKitties = MutableLiveData<List<Like>>()
    val likedKitties: LiveData<List<Like>> = _likedKitties

    private var breedFilter: Breed? = null
    private var likedLoadingJob: Job = loadLiked()

    private val kittiesPagingFactory = InvalidatingPagingSourceFactory {
        KittiesPagingSource(repository, breedFilter, likedKitties.value ?: listOf())
    }
    private val kittiesPager =
        Pager(PagingConfig(pageSize = 6), pagingSourceFactory = kittiesPagingFactory)
    val kitties: Flow<PagingData<Kitty>> = kittiesPager
        .flow
        .cachedIn(viewModelScope)

    init {
        loadLiked()
    }

    private fun loadLiked(): Job {
        return viewModelScope.launch {
            _likedKitties.value = repository.fetchLiked().map { it.asLike() }
//                kittiesPagingFactory.invalidate()
        }
    }

    fun likeKitty(kitty: Kitty) {
        viewModelScope.launch {
            repository.likeKitty(kitty)
        }
    }

    fun applyFilter(breed: Breed?) {
        if (breed == breedFilter) return // don't reapply same filter
        breedFilter = breed
        kittiesPagingFactory.invalidate()
    }
}


class KittiesPagingSource(
    private val repository: Repository,
    var breedFilter: Breed? = null,
    var likedKitties: List<Like> = listOf()
) : PagingSource<Int, Kitty>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Kitty> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 1

            val filters = if (breedFilter != null) listOf(breedFilter!!) else null
            var kitties: List<KittyRemoteModel> = listOf()
            try {
                kitties = repository.fetchKitties(nextPage, KITTIES_PAGE_SIZE, filters)
            } catch (e: Throwable) {
                e.printStackTrace()
            }

            val newKitties = kitties.map { kitty ->
                kitty.asKitty(likedKitties.any { it.imageId == kitty.id })
            }

            LoadResult.Page(
                data = newKitties,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (newKitties.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Kitty>): Int? {
        return state.anchorPosition?.let {
            if (it < state.config.initialLoadSize) {
                // if anchor position is less than initial loading count then download from the beginning
                0
            } else {
                // otherwise load a page around anchorPosition using initialLoadSize
                (it - state.config.initialLoadSize / 2).coerceAtLeast(0)
            }
        }
    }
}
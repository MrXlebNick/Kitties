package com.xlebnick.kitties.utils

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.xlebnick.kitties.ui.base.BaseFragment
import com.xlebnick.kitties.ui.main.MainViewModel
import com.xlebnick.kitties.ui.main.RequestError
import kotlinx.coroutines.flow.collect

fun <Binding : ViewBinding> BaseFragment<Binding>.registerErrorListener(viewModel: MainViewModel) {
    lifecycleScope.launchWhenResumed {
        viewModel.error.collect { error ->
            val message = when (error) {
                is RequestError.LikeRequestError -> "Like failed with message: ${error.message}"
                is RequestError.UnlikeRequestError -> "Dislike failed with message: ${error.message}"
                is RequestError.FetchRequestError -> "Fetching kitties failed with message: ${error.message}"
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }
}
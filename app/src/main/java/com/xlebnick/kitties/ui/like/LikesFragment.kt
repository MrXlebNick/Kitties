package com.xlebnick.kitties.ui.like

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xlebnick.kitties.databinding.FragmentLikesBinding
import com.xlebnick.kitties.ui.base.BaseFragment
import com.xlebnick.kitties.ui.main.MainViewModel
import com.xlebnick.kitties.utils.registerErrorListener

/**
 * A fragment representing a list of Items.
 */
class LikesFragment : BaseFragment<FragmentLikesBinding>() {

    private val viewModel: MainViewModel by diActivityViewModels()
    private lateinit var listAdapter: LikesAdapter

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): FragmentLikesBinding =
        FragmentLikesBinding.inflate(inflater, parent, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListView()
        setupLikeButton()
        subscribeToViewModel()

        registerErrorListener(viewModel)
    }

    private fun setupLikeButton() {
        binding?.likeButton?.setOnClickListener {
            navControllerHelper.navigateTo(LikesFragmentDirections.actionLikedFragmentToMain())
        }
    }

    private fun setupListView() {
        binding?.listView?.run {
            listAdapter = LikesAdapter(
                onLikeClick = viewModel::unlikeKitty
            ).also {
                adapter = it
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.likedKitties.observe(viewLifecycleOwner) { kitties ->
            listAdapter.setData(kitties)
        }
    }
}
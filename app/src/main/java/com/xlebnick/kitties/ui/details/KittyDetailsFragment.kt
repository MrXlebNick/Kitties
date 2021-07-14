package com.xlebnick.kitties.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.xlebnick.kitties.R
import com.xlebnick.kitties.databinding.FragmentKittyDetailsBinding
import com.xlebnick.kitties.ui.base.BaseFragment
import com.xlebnick.kitties.ui.main.MainViewModel

class KittyDetailsFragment : BaseFragment<FragmentKittyDetailsBinding>() {

    private val args: KittyDetailsFragmentArgs by navArgs()

    private val viewModel: MainViewModel by diActivityViewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentKittyDetailsBinding = FragmentKittyDetailsBinding.inflate(inflater, parent, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            with(args.kitty) {
                idView.text = id
                breedsView.text = breeds.map { it.name }.joinToString(", ")
                Glide.with(requireContext())
                    .load(url)
                    .into(image)

                likeButton.setImageResource(if (isLiked) R.drawable.ic_liked else R.drawable.ic_like)
                likeButton.setOnClickListener { viewModel.toggleLikeKitty(this) }
            }
        }
    }
}
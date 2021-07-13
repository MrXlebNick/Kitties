package com.xlebnick.kitties.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.xlebnick.kitties.R
import com.xlebnick.kitties.data.model.Kitty
import com.xlebnick.kitties.databinding.KittiesListItemBinding

class KittiesAdapter(
    private val onClick: (Kitty) -> Unit,
    private val onLikeClick: (Kitty) -> Unit
) :
    PagingDataAdapter<Kitty, KittiesAdapter.KittiesViewHolder>(KittyComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KittiesViewHolder {
        return KittiesViewHolder(
            KittiesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: KittiesViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return, onClick, onLikeClick)
    }

    class KittiesViewHolder(private val binding: KittiesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(kitty: Kitty, onClick: (Kitty) -> Unit, onLikeClick: (Kitty) -> Unit) {
            Glide.with(binding.image)
                .load(kitty.url)
                .transform(CenterCrop())
                .into(binding.image)

            binding.breeds.text = kitty.breeds.joinToString(", ") { it.name }
            binding.likeButton.setImageResource(if (kitty.isLiked) R.drawable.ic_liked else R.drawable.ic_like)

            binding.likeButton.setOnClickListener { onLikeClick(kitty) }
            binding.root.setOnClickListener { onClick(kitty) }
        }
    }

    object KittyComparator : DiffUtil.ItemCallback<Kitty>() {
        override fun areItemsTheSame(oldItem: Kitty, newItem: Kitty): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Kitty, newItem: Kitty): Boolean {
            return oldItem == newItem
        }
    }

}
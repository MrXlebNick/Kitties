package com.xlebnick.kitties.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
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
    RecyclerView.Adapter<KittiesAdapter.KittiesViewHolder>() {

    private val data = mutableListOf<Kitty>()

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
        holder.bind(data[position], onClick, onLikeClick)
    }

    override fun getItemCount(): Int = data.size

    fun setData(data: Collection<Kitty>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
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
}
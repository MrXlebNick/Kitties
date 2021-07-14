package com.xlebnick.kitties.ui.like

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.xlebnick.kitties.R
import com.xlebnick.kitties.data.model.Like
import com.xlebnick.kitties.databinding.KittiesListItemBinding

class LikesAdapter(
    private val onLikeClick: (Like) -> Unit
) :
    RecyclerView.Adapter<LikesAdapter.LikesViewHolder>() {

    private val data = mutableListOf<Like>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikesViewHolder {
        return LikesViewHolder(
            KittiesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LikesViewHolder, position: Int) {
        holder.bind(data[position], onLikeClick)
    }

    override fun getItemCount(): Int = data.size

    fun setData(data: Collection<Like>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class LikesViewHolder(private val binding: KittiesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(like: Like, onLikeClick: (Like) -> Unit) {
            Glide.with(binding.image)
                .load(like.imageUrl)
                .transform(CenterCrop())
                .into(binding.image)

            binding.likeButton.setImageResource(R.drawable.ic_liked)

            binding.likeButton.setOnClickListener {
                onLikeClick(like)
                binding.likeButton.setImageResource(R.drawable.ic_like)
            }
        }
    }
}
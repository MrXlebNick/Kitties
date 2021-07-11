package com.xlebnick.kitties.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.xlebnick.kitties.R
import com.xlebnick.kitties.data.Kitty
import com.xlebnick.kitties.databinding.KittiesListItemBinding
import com.xlebnick.kitties.databinding.MainFragmentBinding
import com.xlebnick.kitties.ui.base.BaseFragment

class KittyListFragment : BaseFragment<MainFragmentBinding>() {

    private val viewModel: MainViewModel by diViewModels()

    private lateinit var listAdapter: KittiesAdapter
    private lateinit var filterAdapter: ArrayAdapter<String>

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): MainFragmentBinding {
        return MainFragmentBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = KittiesAdapter(
            onClick = { kitty ->
                navControllerHelper.navigateTo(KittyListFragmentDirections.actionMainToDetails(kitty))
            },
            onLikeClick = viewModel::likeKitty
        ).also {
            binding?.listView?.adapter = it
        }

        filterAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.filter?.adapter = adapter
        }
        binding?.filter?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.applyFilter((view as TextView).text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.applyFilter(null)
            }
        }

        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewModel.breeds.observe(viewLifecycleOwner) { breeds ->
            filterAdapter.clear()
            filterAdapter.addAll(breeds)
        }
    }
}

class KittiesAdapter(
    private val onClick: (Kitty) -> Unit,
    private val onLikeClick: (Kitty) -> Unit
) :
    RecyclerView.Adapter<KittiesAdapter.KittiesViewHolder>() {

    private val data = mutableListOf<Kitty>(
        Kitty(
            "https://25.media.tumblr.com/tumblr_maklk6AJiw1qhwmnpo1_400.jpg",
            "1",
            listOf(),
            listOf(),
            true
        ),
        Kitty(
            "https://cdn2.thecatapi.com/images/ckk.gif",
            "2",
            listOf(),
            listOf("some", "breed"),
            false
        )
    )

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

    class KittiesViewHolder(private val binding: KittiesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(kitty: Kitty, onClick: (Kitty) -> Unit, onLikeClick: (Kitty) -> Unit) {
            Glide.with(binding.image)
                .load(kitty.url)
                .transform(CenterCrop())
                .into(binding.image)

            binding.breeds.text = kitty.breeds.joinToString(", ")
            binding.likeButton.setImageResource(if (kitty.isLiked) R.drawable.ic_liked else R.drawable.ic_like)

            binding.likeButton.setOnClickListener { onLikeClick(kitty) }
            binding.root.setOnClickListener { onClick(kitty) }
        }
    }
}

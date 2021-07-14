package com.xlebnick.kitties.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.xlebnick.kitties.databinding.MainFragmentBinding
import com.xlebnick.kitties.ui.base.BaseFragment

class KittyListFragment : BaseFragment<MainFragmentBinding>() {

    private val viewModel: MainViewModel by diActivityViewModels()

    private lateinit var listAdapter: KittiesAdapter
    private lateinit var filterAdapter: ArrayAdapter<String>

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): MainFragmentBinding {
        return MainFragmentBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListView()

        setupFilters()

        setupFab()

        subscribeToViewModel()
    }

    private fun setupFab() {
        binding?.fab?.setOnClickListener {
            navControllerHelper.navigateTo(KittyListFragmentDirections.actionMainToCameraFragment())
        }
    }

    private fun setupListView() {
        binding?.listView?.run {
            listAdapter = KittiesAdapter(
                onClick = { kitty ->
                    navControllerHelper.navigateTo(
                        KittyListFragmentDirections.actionMainToDetails(
                            kitty
                        )
                    )
                },
                onLikeClick = viewModel::toggleLikeKitty
            ).also {
                adapter = it
            }
        }
    }

    private fun setupFilters() {
        filterAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.filter?.adapter = adapter
        }
        binding?.filter?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            private var isHumanInteraction = false
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!isHumanInteraction) {
                    isHumanInteraction = true
                    return
                }
                viewModel.applyFilter(viewModel.breeds.value?.getOrNull(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                if (!isHumanInteraction) {
                    isHumanInteraction = true
                    return
                }
                viewModel.applyFilter(null)
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.breeds.observe(viewLifecycleOwner) { breeds ->
            filterAdapter.clear()
            filterAdapter.addAll(breeds.map { it.name })
        }

        viewModel.kitties.observe(viewLifecycleOwner) { kitties ->
            listAdapter.setData(kitties)
        }
    }
}


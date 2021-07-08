package com.xlebnick.kitties.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.xlebnick.kitties.databinding.MainFragmentBinding
import com.xlebnick.kitties.ui.base.BaseFragment

class MainFragment : BaseFragment<MainFragmentBinding>() {

    private val viewModel: MainViewModel by diViewModels()

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): MainFragmentBinding {
        return MainFragmentBinding.inflate(inflater, parent, false)
    }

}
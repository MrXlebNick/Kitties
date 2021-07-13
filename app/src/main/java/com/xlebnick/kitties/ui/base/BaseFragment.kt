package com.xlebnick.kitties.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.viewbinding.ViewBinding
import com.xlebnick.kitties.utils.FragmentNavControllerHelper
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<Binding : ViewBinding> : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var navControllerHelper: FragmentNavControllerHelper

    protected var binding: Binding? = null

    @CallSuper
    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createBinding(inflater, container)
        return binding!!.root // safe to !! because the initiation happens in the same body
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        navControllerHelper.setNavController(findNavController())
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        navControllerHelper.setNavController(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    abstract fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): Binding

    protected inline fun <reified VM : ViewModel> diViewModels() =
        viewModels<VM> { viewModelFactory }

    protected inline fun <reified VM : ViewModel> diActivityViewModels() =
        activityViewModels<VM> { viewModelFactory }

    protected inline fun <reified VM : ViewModel> diNavGraphViewModels(@IdRes navGraphId: Int) =
        navGraphViewModels<VM>(navGraphId) { viewModelFactory }

}
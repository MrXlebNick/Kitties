package com.xlebnick.kitties.di.modules

import androidx.lifecycle.ViewModel
import com.xlebnick.kitties.di.ViewModelKey
import com.xlebnick.kitties.ui.camera.CameraViewModel
import com.xlebnick.kitties.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CameraViewModel::class)
    abstract fun cameraViewModel(viewModel: CameraViewModel): ViewModel
}
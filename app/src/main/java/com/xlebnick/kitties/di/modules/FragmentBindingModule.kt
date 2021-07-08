package com.xlebnick.kitties.di.modules

import com.xlebnick.kitties.di.FragmentScoped
import com.xlebnick.kitties.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun mainFragment(): MainFragment

}

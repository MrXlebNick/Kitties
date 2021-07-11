package com.xlebnick.kitties.di.modules

import com.xlebnick.kitties.di.FragmentScoped
import com.xlebnick.kitties.ui.details.KittyDetailsFragment
import com.xlebnick.kitties.ui.main.KittyListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun KittyListFragment(): KittyListFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun kittyDetailsFragment(): KittyDetailsFragment

}

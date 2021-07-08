package com.xlebnick.kitties.di.modules

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.xlebnick.kitties.MainActivity
import com.xlebnick.kitties.di.ActivityScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Named

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class,
            FragmentBindingModule::class
        ]
    )
    abstract fun coreMainActivity(): MainActivity
}


@Module
abstract class MainActivityModule {

    @Binds
    @Named("activity")
    abstract fun provideActivityContext(activity: MainActivity): Context

    @Binds
    abstract fun provideLifecycleOwner(activity: MainActivity): LifecycleOwner
}

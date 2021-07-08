package com.xlebnick.kitties

import android.app.Application
import com.xlebnick.kitties.di.DaggerMainComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class App: Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        initDependencyInjection()
    }

    private fun initDependencyInjection() {
        val mainComponent = DaggerMainComponent // generated on build phase; if it's highlighted red, just build the project
            .builder()
            .application(this)
            .build()

        mainComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

}
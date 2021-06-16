package com.example.atlantapp

import android.app.Application
import com.example.atlantapp.auth.AuthModule
import com.example.atlantapp.common.CommonModule
import com.example.atlantapp.main.MainModule
import com.example.atlantapp.root.RootModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.KoinContextHandler
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupTimber()
    }

    private fun setupKoin() {
        KoinContextHandler.stop()
        startKoin {
            androidContext(this@App)
            modules(
                CommonModule.create(),
                AuthModule.create(),
                MainModule.create(),
                RootModule.create()
            )
        }
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
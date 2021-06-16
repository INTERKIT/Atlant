package com.example.atlantapp.root

import com.example.atlantapp.root.ui.RootContract
import com.example.atlantapp.root.ui.RootPresenter
import org.koin.dsl.bind
import org.koin.dsl.module

object RootModule {

    fun create() = module {
        single { RootPresenter(get()) } bind RootContract.Presenter::class
    }
}
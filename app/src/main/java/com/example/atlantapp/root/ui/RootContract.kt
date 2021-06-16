package com.example.atlantapp.root.ui

import com.example.atlantapp.common.mvp.MvpPresenter
import com.example.atlantapp.common.mvp.MvpView

interface RootContract {
    interface View : MvpView {
        fun navigateToAuth()
        fun navigateToMain()
    }

    interface Presenter : MvpPresenter<View> {
        fun openInitialScreen()
    }
}
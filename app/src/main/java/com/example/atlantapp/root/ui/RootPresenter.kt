package com.example.atlantapp.root.ui

import com.example.atlantapp.auth.interactor.AuthInteractor
import com.example.atlantapp.common.mvp.BasePresenter

class RootPresenter(
    private val authInteractor: AuthInteractor
) : BasePresenter<RootContract.View>(), RootContract.Presenter {

    override fun openInitialScreen() {
        if (authInteractor.isRegistered()) {
            view?.navigateToMain()
        } else {
            view?.navigateToAuth()
        }
    }
}
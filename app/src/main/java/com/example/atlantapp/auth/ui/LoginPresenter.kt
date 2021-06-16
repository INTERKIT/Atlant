package com.example.atlantapp.auth.ui

import com.example.atlantapp.R
import com.example.atlantapp.auth.interactor.AuthInteractor
import com.example.atlantapp.common.mvp.BasePresenter
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginPresenter(
    private val authInteractor: AuthInteractor
) : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    override fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            view?.showMessage(R.string.auth_invalid_credentials)
            return
        }

        view?.showLoading(true)
        launch {
            try {
                authInteractor.signIn(email, password)
                view?.showSuccess()
            } catch (e: Throwable) {
                Timber.e(e, "Error signing in")
                view?.showMessage(R.string.error_general_message)
            } finally {
                view?.showLoading(false)
            }
        }
    }
}
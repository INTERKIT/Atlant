package com.example.atlantapp.auth.ui

import androidx.annotation.StringRes
import com.example.atlantapp.common.mvp.MvpPresenter
import com.example.atlantapp.common.mvp.MvpView

interface LoginContract {

    interface View : MvpView {
        fun showLoading(isLoading: Boolean)
        fun showMessage(@StringRes resId: Int)
        fun showSuccess()
    }

    interface Presenter : MvpPresenter<View> {
        fun signIn(email: String, password: String)
    }
}
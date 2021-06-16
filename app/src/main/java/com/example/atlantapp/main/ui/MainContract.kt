package com.example.atlantapp.main.ui

import androidx.annotation.StringRes
import com.example.atlantapp.common.mvp.MvpPresenter
import com.example.atlantapp.common.mvp.MvpView
import com.example.atlantapp.main.model.Profile
import com.example.atlantapp.main.model.Transaction
import java.math.BigDecimal
import java.math.BigInteger

interface MainContract {

    interface View : MvpView {
        fun showTransactions(transactions: List<Transaction>)
        fun showBalance(balance: BigInteger)
        fun showLoading(isLoading: Boolean)
        fun showProfile(profile: Profile)
        fun showError(@StringRes resId: Int)
        fun navigateToAuth()
    }

    interface Presenter : MvpPresenter<View> {
        fun loadProfile()
        fun loadTransactions()
        fun logout()
        fun startUpdates()
        fun stopUpdates()
        fun clearUpdates()
    }
}
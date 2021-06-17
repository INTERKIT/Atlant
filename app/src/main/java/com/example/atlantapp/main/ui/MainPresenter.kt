package com.example.atlantapp.main.ui

import com.example.atlantapp.R
import com.example.atlantapp.auth.interactor.AuthInteractor
import com.example.atlantapp.common.mvp.BasePresenter
import com.example.atlantapp.main.interactor.MainInteractor
import com.example.atlantapp.main.model.ProfileResult
import com.example.atlantapp.socket.UpdatesManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigInteger
import kotlin.properties.Delegates

class MainPresenter(
    private val authInteractor: AuthInteractor,
    private val mainInteractor: MainInteractor,
    private val updatesManager: UpdatesManager,
) : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private var balance by Delegates.observable(BigInteger.ZERO) { _, _, newValue ->
        view?.showBalance(newValue)
    }

    override fun detach() {
        updatesManager.stop()
        super.detach()
    }

    override fun loadProfile() {
        launch {
            try {
                view?.showLoading(true)
                val result = mainInteractor.getProfile()
                handleResult(result)
            } catch (e: Throwable) {
                Timber.e(e, "Error loading profile data")
                view?.showError(R.string.error_general_message)
            } finally {
                view?.showLoading(false)
            }
        }
    }

    override fun loadTransactions() {
        launch {
            mainInteractor.getTransactionsFlow().collectLatest { transactions ->
                view?.showTransactions(transactions)

                if (transactions.isNotEmpty()) {
                    balance = transactions.map { it.value }.fold(BigInteger.ZERO, BigInteger::add)
                }
            }
        }
    }

    override fun logout() {
        updatesManager.stop()
        authInteractor.logout()

        view?.navigateToAuth()
    }

    override fun startUpdates() {
        updatesManager.start()
    }

    override fun stopUpdates() {
        updatesManager.stop()
    }

    override fun clearUpdates() {
        mainInteractor.clearMemory()
        balance = BigInteger.ZERO
    }

    private fun handleResult(result: ProfileResult) {
        when (result) {
            is ProfileResult.Success -> view?.showProfile(result.data)
            is ProfileResult.Failed -> view?.showError(R.string.error_profile_not_found)
        }
    }
}
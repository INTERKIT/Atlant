package com.example.atlantapp.auth

import android.content.Context
import com.example.atlantapp.R
import com.example.atlantapp.auth.api.AuthApi
import com.example.atlantapp.auth.interactor.AuthInteractor
import com.example.atlantapp.auth.repository.AuthRemoteRepository
import com.example.atlantapp.auth.repository.AuthRepository
import com.example.atlantapp.auth.ui.LoginContract
import com.example.atlantapp.auth.ui.LoginPresenter
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

object AuthModule {

    fun create() = module {
        single {
            val api = get<Retrofit>().create(AuthApi::class.java)
            AuthRemoteRepository(api)
        } bind AuthRepository::class

        single {
            val sessionId = get<Context>().getString(R.string.sessionId)
            AuthInteractor(sessionId, get(), get())
        }
        single { LoginPresenter(get()) } bind LoginContract.Presenter::class
    }
}
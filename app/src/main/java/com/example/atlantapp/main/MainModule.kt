package com.example.atlantapp.main

import com.example.atlantapp.main.api.MainApi
import com.example.atlantapp.main.interactor.MainInteractor
import com.example.atlantapp.main.repository.MainInMemoryRepository
import com.example.atlantapp.main.repository.MainLocalRepository
import com.example.atlantapp.main.repository.MainRemoteRepository
import com.example.atlantapp.main.repository.MainRepository
import com.example.atlantapp.main.ui.MainContract
import com.example.atlantapp.main.ui.MainPresenter
import com.example.atlantapp.socket.UpdateHandler
import com.example.atlantapp.socket.handler.TransactionHandler
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

object MainModule {

    fun create() = module {
        single { MainInMemoryRepository() } bind MainLocalRepository::class
        factory { MainInteractor(get(), get()) }
        single {
            val api = get<Retrofit>().create(MainApi::class.java)
            MainRemoteRepository(api)
        } bind MainRepository::class

        single { MainPresenter(get(), get(), get()) } bind MainContract.Presenter::class

        single(named<UpdateHandler>()) {
            listOf(TransactionHandler(get(), get()))
        }
    }
}
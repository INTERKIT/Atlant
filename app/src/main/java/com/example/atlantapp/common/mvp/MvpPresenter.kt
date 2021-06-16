package com.example.atlantapp.common.mvp

interface MvpPresenter<V : MvpView> {

    fun attach(view: V)

    fun detach()
}
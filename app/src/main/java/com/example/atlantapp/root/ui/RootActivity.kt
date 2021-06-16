package com.example.atlantapp.root.ui

import android.os.Bundle
import com.example.atlantapp.R
import com.example.atlantapp.auth.ui.LoginFragment
import com.example.atlantapp.common.mvp.BaseMvpActivity
import com.example.atlantapp.main.ui.MainFragment
import com.example.atlantapp.utils.popBackStack
import com.example.atlantapp.utils.replace
import org.koin.android.ext.android.inject

class RootActivity : BaseMvpActivity<RootContract.View, RootContract.Presenter>(), RootContract.View {

    override val presenter: RootContract.Presenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        if (savedInstanceState == null) {
            presenter.openInitialScreen()
        }
    }

    override fun navigateToAuth() {
        replace(LoginFragment.create())
    }

    override fun navigateToMain() {
        replace(MainFragment.create())
    }

    override fun onBackPressed() {
        popBackStack()
    }
}
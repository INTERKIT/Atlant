package com.example.atlantapp.auth.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.atlantapp.R
import com.example.atlantapp.common.mvp.BaseMvpFragment
import com.example.atlantapp.databinding.FragmentLoginBinding
import com.example.atlantapp.main.ui.MainFragment
import com.example.atlantapp.utils.popAndReplace
import org.koin.android.ext.android.inject

class LoginFragment :
    BaseMvpFragment<LoginContract.View, LoginContract.Presenter>(R.layout.fragment_login),
    LoginContract.View {

    companion object {
        fun create() = LoginFragment()
    }

    override val presenter: LoginContract.Presenter by inject()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            emailEditText.setText("hello@karta.com")
            passwordEditText.setText("12345678")

            signInButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val pw = passwordEditText.text.toString()
                presenter.signIn(email, pw)
            }
        }
    }

    override fun showSuccess() {
        popAndReplace(
            target = MainFragment.create(),
            inclusive = true
        )
    }

    override fun showLoading(isLoading: Boolean) {
        with(binding) {
            progressBar.isVisible = isLoading
            signInButton.isVisible = !isLoading
        }
    }

    override fun showMessage(resId: Int) {
        Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
    }
}
package com.example.atlantapp.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.atlantapp.R
import com.example.atlantapp.auth.ui.LoginFragment
import com.example.atlantapp.common.mvp.BaseMvpFragment
import com.example.atlantapp.databinding.FragmentMainBinding
import com.example.atlantapp.main.model.Profile
import com.example.atlantapp.main.model.Transaction
import com.example.atlantapp.main.ui.adapter.MainAdapter
import com.example.atlantapp.utils.popAndReplace
import org.koin.android.ext.android.inject
import java.math.BigInteger

class MainFragment :
    BaseMvpFragment<MainContract.View, MainContract.Presenter>(R.layout.fragment_main),
    MainContract.View {

    companion object {
        fun create() = MainFragment()
    }

    override val presenter: MainContract.Presenter by inject()

    private lateinit var binding: FragmentMainBinding

    private val mainAdapter: MainAdapter by lazy {
        MainAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.inflateMenu(R.menu.main_menu)
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.logoutId) {
                    presenter.logout()
                    return@setOnMenuItemClickListener true
                }

                return@setOnMenuItemClickListener false
            }

            startButton.setOnClickListener { presenter.startUpdates() }
            stopButton.setOnClickListener { presenter.stopUpdates() }
            clearButton.setOnClickListener { presenter.clearUpdates() }

            with(transactionsRecyclerView) {
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    reverseLayout = true
                    stackFromEnd = true
                }
                adapter = mainAdapter
            }
        }

        presenter.loadProfile()
        presenter.loadTransactions()
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    override fun showProfile(profile: Profile) {
        with(binding) {
            nameTextView.text = profile.fullName
            typeTextView.text = profile.type
        }
    }

    override fun showBalance(balance: BigInteger) {
        binding.balanceTextView.text = getString(R.string.main_total_format, balance.toString())
    }

    override fun showTransactions(transactions: List<Transaction>) {
        mainAdapter.setItems(transactions)
        binding.transactionsRecyclerView.scrollToPosition(transactions.size - 1)

        val isEmpty = transactions.isEmpty()
        binding.transactionsRecyclerView.isVisible = !isEmpty
        binding.emptyTextView.isVisible = isEmpty
    }

    override fun navigateToAuth() {
        popAndReplace(
            target = LoginFragment.create(),
            inclusive = true
        )
    }

    override fun showError(resId: Int) {
        Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
    }
}
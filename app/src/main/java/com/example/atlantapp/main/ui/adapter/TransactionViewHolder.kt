package com.example.atlantapp.main.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.atlantapp.R
import com.example.atlantapp.databinding.ItemTransactionBinding
import com.example.atlantapp.main.model.Transaction

class TransactionViewHolder(
    binding: ItemTransactionBinding
) : RecyclerView.ViewHolder(binding.root) {

    constructor(
        parent: ViewGroup,
    ) : this(
        ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    private val addressTextView = binding.addressTextView
    private val valueTextView = binding.valueTextView

    fun onBind(item: Transaction) {
        addressTextView.text = itemView.context.getString(
            R.string.main_address_format, item.getFormattedAddress()
        )

        valueTextView.text = itemView.context.getString(
            R.string.main_value_format, item.value.toString()
        )

    }
}
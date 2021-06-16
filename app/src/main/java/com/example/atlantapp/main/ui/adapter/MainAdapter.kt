package com.example.atlantapp.main.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.atlantapp.main.model.Transaction

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = mutableListOf<Transaction>()

    fun setItems(new: List<Transaction>) {
        val old = ArrayList(data)
        data.clear()
        data.addAll(new)

        DiffUtil.calculateDiff(getDiffCallback(old, data)).dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        TransactionViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TransactionViewHolder).onBind(data[position])
    }

    private fun getDiffCallback(
        oldList: List<Transaction>,
        newList: List<Transaction>
    ) = object : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]
            return old.address == new.address && old.value == new.value
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]
            return old == new
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }
}
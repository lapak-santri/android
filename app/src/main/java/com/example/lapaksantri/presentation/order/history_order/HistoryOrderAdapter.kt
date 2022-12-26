package com.example.lapaksantri.presentation.order.history_order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.AllOrderItemBinding
import com.example.lapaksantri.domain.entities.Transaction
import com.example.lapaksantri.presentation.order.order_detail.DetailOrderAdapter
import com.example.lapaksantri.utils.formatDate
import com.example.lapaksantri.utils.formatRupiah

class HistoryOrderAdapter(private val clickListener: (Transaction) -> Unit) : ListAdapter<Transaction, HistoryOrderAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(private val context: Context, private  val binding: AllOrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction, listener: (Transaction) -> Unit) {
            with(binding) {
                tvName.text = transaction.name
                tvId.text = transaction.invoice
                tvTotalPrice.text = formatRupiah(transaction.priceTotal.toDouble())
                tvCity.text = transaction.districtName
                tvAddress.text = transaction.address
                tvDate.text = formatDate(transaction.createdAt)

                if (transaction.status == "success") {
                    constraintLayoutStatus.setBackgroundResource(R.drawable.already_paid_order_bg)
                    ivStatus.setImageResource(R.drawable.ic_already_paid)
                    tvStatus.text = context.getString(R.string.already_paid)
                }
                if (transaction.status == "failed") {
                    ivStatus.setImageResource(R.drawable.ic_error)
                    tvStatus.text = context.getString(R.string.order_failed)
                }

                root.setOnClickListener {
                    listener(transaction)
                }

                val detailOrderAdapter = DetailOrderAdapter()
                rvItem.layoutManager = LinearLayoutManager(context)
                rvItem.adapter = detailOrderAdapter
                detailOrderAdapter.submitList(transaction.carts)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = AllOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction, clickListener)
    }
}
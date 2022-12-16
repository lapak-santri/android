package com.example.lapaksantri.presentation.order.order_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lapaksantri.databinding.DetailOrderItemBinding
import com.example.lapaksantri.domain.entities.Cart
import com.example.lapaksantri.utils.formatRupiah

class DetailOrderAdapter : ListAdapter<Cart, DetailOrderAdapter.ViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: DetailOrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart) {
            with(binding) {
                tvName.text = cart.name
                tvQuantity.text = cart.quantity.toString()
                tvPrice.text = formatRupiah(cart.price * cart.quantity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DetailOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = getItem(position)
        holder.bind(detail)
    }
}
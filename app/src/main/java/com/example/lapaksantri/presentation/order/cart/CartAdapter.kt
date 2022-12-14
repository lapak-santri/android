package com.example.lapaksantri.presentation.order.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lapaksantri.databinding.OrderItemBinding
import com.example.lapaksantri.domain.entities.Cart
import com.example.lapaksantri.utils.formatRupiah

class CartAdapter(private val clickListener: (Int, String) -> Unit, private val deleteListener: (List<Int>) -> Unit) : ListAdapter<Cart, CartAdapter.ViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart, listener: (Int, String) -> Unit, deleteListener: (List<Int>) -> Unit) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(cart.imagePath)
                    .into(ivProduct)
                tvName.text = cart.name
                tvTotalPriceItem.text = formatRupiah(cart.quantity * cart.price)
                tvPrice.text = formatRupiah(cart.price)
                tvQuantity.text = cart.quantity.toString()

                btnDelete.setOnClickListener {
                    deleteListener(cart.cartId)
                }
                btnPlus.setOnClickListener {
                    listener(cart.id, "plus")
                }
                btnMinus.setOnClickListener {
                    if (cart.quantity > 1) {
                        listener(cart.id, "min")
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order, clickListener, deleteListener)
    }
}
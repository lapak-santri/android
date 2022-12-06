package com.example.lapaksantri.presentation.order.add_order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.ProductItemBinding
import com.example.lapaksantri.domain.entities.Product
import com.example.lapaksantri.utils.visible

class AddOrderAdapter(private val plusListener: (Product, Int) -> Unit, private val minListener: (Product, Int) -> Unit) : ListAdapter<Product, AddOrderAdapter.OrderViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    class OrderViewHolder(private val context: Context, private val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product, plusListener: (Product, Int) -> Unit, minListener: (Product, Int) -> Unit) {
            with(binding) {
                if (product.stock == 0) {
                    tvOutOfStock.visible()
                    btnMinus.isEnabled = false
                    btnPlus.isEnabled = false
                    btnMinus.setImageResource(R.drawable.ic_minus_grey)
                    btnPlus.setImageResource(R.drawable.ic_plus_grey)
                }
                val resources = context.resources
                Glide.with(itemView.context)
                    .load(product.imagePath)
                    .into(ivProduct)
                tvName.text = product.name
                tvPrice.text = product.price.toString()
                tvUnitName.text = resources.getString(R.string.unit_name, product.unitName)
                tvQuantity.text = product.quantityInCart.toString()
                btnPlus.setOnClickListener {
                    product.quantityInCart++
                    plusListener(product, adapterPosition)
                }
                btnMinus.setOnClickListener {
                    if (product.quantityInCart > 0) {
                        product.quantityInCart--
                        minListener(product, adapterPosition)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, plusListener, minListener)
    }

}
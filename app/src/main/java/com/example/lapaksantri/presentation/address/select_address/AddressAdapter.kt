package com.example.lapaksantri.presentation.address.select_address

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.AddressItemBinding
import com.example.lapaksantri.domain.entities.Address

class AddressAdapter(private val clickListener: (Int) -> Unit, private val editAddressListener: (Address) -> Unit) : ListAdapter<Address, AddressAdapter.ViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    private var lastMainAddress = -1

    inner class ViewHolder(private val context: Context, private val binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address) {
            with(binding) {
                val resources = context.resources
                tvName.text = address.recipient
                tvAddress.text = address.detailAddress
                tvType.text = resources.getString(R.string.address_type, address.area, address.phone.replace("+62", "0"))
                if (address.isMain) {
                    root.setBackgroundResource(R.drawable.selected_address)
                    radioButton.isChecked = true
                    lastMainAddress = adapterPosition
                    clickListener(address.id)
                } else {
                    root.setBackgroundResource(R.drawable.order_bg)
                    radioButton.isChecked = false
                }
                binding.btnChangeAddress.setOnClickListener {
                    editAddressListener(address)
                }
                root.setOnClickListener {
                    if (!address.isMain) {
                        address.isMain = true
                        if (lastMainAddress != -1) {
                            getItem(lastMainAddress).isMain = false
                        }
                        notifyItemChanged(adapterPosition)
                        notifyItemChanged(lastMainAddress)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = getItem(position)
        holder.bind(address)

    }

}
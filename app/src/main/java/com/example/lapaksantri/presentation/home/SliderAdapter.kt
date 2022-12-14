package com.example.lapaksantri.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lapaksantri.databinding.SliderItemBinding
import com.example.lapaksantri.domain.entities.Slider

class SliderAdapter : ListAdapter<Slider, SliderAdapter.SliderViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Slider>() {
        override fun areItemsTheSame(oldItem: Slider, newItem: Slider): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Slider, newItem: Slider): Boolean {
            return oldItem == newItem
        }
    }

    class SliderViewHolder(private val binding: SliderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(slider: Slider) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(slider.imagePath)
                    .into(imgSlider)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = SliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val slider = getItem(position)
        holder.bind(slider)
    }
}
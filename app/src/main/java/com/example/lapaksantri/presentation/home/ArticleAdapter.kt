package com.example.lapaksantri.presentation.home

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.ArticleItemBinding
import com.example.lapaksantri.domain.entities.Article

class ArticleAdapter(private val clickListener: (Article) -> Unit) : ListAdapter<Article, ArticleAdapter.ViewHolder>(
    DiffCallback
) {

    companion object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(private val context: Context, private val binding: ArticleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, clickListener: (Article) -> Unit) {
            with(binding) {
                val resources = context.resources
                Glide.with(itemView.context)
                .load(article.imagePath)
                .into(ivThumbnail)
                tvTitle.text = article.title
                tvDate.text = resources.getString(R.string.author_date, "Admin", article.publishedAt)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvDesc.text = Html.fromHtml(article.description, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    tvDesc.text = Html.fromHtml(article.description)
                }

                root.setOnClickListener {
                    clickListener(article)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article, clickListener)
    }
}
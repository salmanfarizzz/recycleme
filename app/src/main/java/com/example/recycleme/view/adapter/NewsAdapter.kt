package com.example.recycleme.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recycleme.R
import com.example.recycleme.data.remote.response.NewsItem
import com.example.recycleme.databinding.NewsLayoutBinding

class NewsAdapter : ListAdapter<NewsItem, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = NewsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    class MyViewHolder(val binding: NewsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NewsItem) {
            binding.tvTitle.text = item.title
            binding.tvLink.apply {
                text = "Read more"
                setTag(R.id.tvLink, item.link)
                visibility = if (item.link != null) View.VISIBLE else View.GONE
            }
            if (!item.thumbnail.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(item.thumbnail)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(binding.imgNews)
            } else {
                binding.imgNews.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_place_holder))
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem.link == newItem.link
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

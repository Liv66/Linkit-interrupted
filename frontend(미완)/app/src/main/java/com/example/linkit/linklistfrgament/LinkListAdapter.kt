package com.example.linkit.linklistfrgament

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit.database.Link
import com.example.linkit.databinding.RvLinkListItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LinkListAdapter(private val clickListener: LinkListener):
    ListAdapter<DataItem, RecyclerView.ViewHolder>(LinkDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addAndSubmitList(list: List<Link>?) {
        adapterScope.launch {
            val items =
                list?.map { DataItem.LinkItem(it) }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val linkItem = getItem(position) as DataItem.LinkItem
                holder.bind(clickListener,linkItem.link)
            }
        }
    }
}

class ViewHolder private constructor(val binding: RvLinkListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: LinkListener, item: Link) {
        binding.link = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RvLinkListItemBinding.inflate(layoutInflater, parent, false)

            return ViewHolder(binding)
        }
    }
}


class LinkDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class LinkListener(val clickListener: (linkId: Link) -> Unit) {
    fun onClick(link: Link) = clickListener(link)
}

sealed class DataItem {

    data class LinkItem(val link: Link) : DataItem() {
        override val id = link.key
    }

    abstract val id: Long
}
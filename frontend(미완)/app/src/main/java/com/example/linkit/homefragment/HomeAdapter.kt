package com.example.linkit.homefragment


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkit.databinding.RvFolderItemBinding
import com.example.linkit.database.Folder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class HomeAdapter(val clickListener1: FolderListener, val clickListener2:FolderListener2) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(FolderDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addAndSubmitList(list: List<Folder>?) {
        adapterScope.launch {
            val items =
                list?.map { DataItem.FolderItem(it) }
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
                val folderItem = getItem(position) as DataItem.FolderItem
                holder.bind(clickListener1,clickListener2, folderItem.folder)
            }
        }
    }
}

class ViewHolder private constructor(val binding: RvFolderItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: FolderListener,clickListener2: FolderListener2, item: Folder) {
        binding.folder = item
        binding.clickListener = clickListener
        binding.clickListener2 = clickListener2
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RvFolderItemBinding.inflate(layoutInflater, parent, false)

            return ViewHolder(binding)
        }
    }
}

class FolderDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class FolderListener(val clickListener: (folderId: Folder) -> Unit) {
    fun onClick(folder: Folder) = clickListener(folder)
}

class FolderListener2(val clickListener: (folderId: Folder) -> Unit) {
    fun onClick(folder: Folder) = clickListener(folder)
}

sealed class DataItem {
    data class FolderItem(val folder: Folder) : DataItem() {
        override val id = folder.key
    }

    abstract val id: Long
}





























//private val ITEM_VIEW_TYPE_POST = 0
//private val ITEM_VIEW_TYPE_FOLDER = 1
//
//class HomeAdapter(val clickListener: FolderListener) :
//    ListAdapter<DataItem, RecyclerView.ViewHolder>(FolderDiffCallback()) {
//
//    private val adapterScope = CoroutineScope(Dispatchers.Default)
//
//    fun addAndSubmitList(list: List<Folder>?) {
//        adapterScope.launch {
//            val items =
//                list?.map { DataItem.FolderItem(it) }
//            withContext(Dispatchers.Main) {
//                submitList(items)
//            }
//        }
//    }
//
//    fun addAndSubmitList2(list: List<Post>?) {
//        adapterScope.launch {
//            val items =
//                list?.map { DataItem.PostItem(it) }
//            withContext(Dispatchers.Main) {
//                submitList(items)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            ITEM_VIEW_TYPE_FOLDER -> FolderViewHolder.from(parent)
//            ITEM_VIEW_TYPE_POST -> PostViewHolder.from(parent)
//            else -> throw ClassCastException("Unknown viewType ${viewType}")
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is FolderViewHolder -> {
//                val folderItem = getItem(position) as DataItem.FolderItem
//                holder.bind(clickListener, folderItem.folder)
//            }
//            is PostViewHolder -> {
//                val postItem = getItem(position) as DataItem.PostItem
//                holder.bind(clickListener, postItem.post)
//            }
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return when (getItem(position)) {
//            is DataItem.FolderItem -> ITEM_VIEW_TYPE_FOLDER
//            is DataItem.PostItem -> ITEM_VIEW_TYPE_POST
//        }
//    }
//}
//
//class PostViewHolder private constructor(val binding: RvFolderItem2Binding) :
//    RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(clickListener: FolderListener, item: Post) {
//        binding.post = item
//        binding.clickListener = clickListener
//        binding.executePendingBindings()
//    }
//
//    companion object {
//        fun from(parent: ViewGroup): PostViewHolder {
//            val layoutInflater = LayoutInflater.from(parent.context)
//            val binding = RvFolderItem2Binding.inflate(layoutInflater, parent, false)
//
//            return PostViewHolder(binding)
//        }
//    }
//}
//
//class FolderViewHolder private constructor(val binding: RvFolderItemBinding) :
//    RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(clickListener: FolderListener, item: Folder) {
//        binding.folder = item
//        binding.clickListener = clickListener
//        binding.executePendingBindings()
//    }
//
//    companion object {
//        fun from(parent: ViewGroup): FolderViewHolder {
//            val layoutInflater = LayoutInflater.from(parent.context)
//            val binding = RvFolderItemBinding.inflate(layoutInflater, parent, false)
//
//            return FolderViewHolder(binding)
//        }
//    }
//}
//
//
//class FolderDiffCallback : DiffUtil.ItemCallback<DataItem>() {
//    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
//        return oldItem == newItem
//    }
//}
//
//class FolderListener(val clickListener: (folderId: Long) -> Unit) {
//    fun onClick(folder: Folder) = clickListener(folder.folderId)
//    fun onClick(post: Post) = clickListener(post.id.toLong())
//}
//
//sealed class DataItem {
//    data class FolderItem(val folder: Folder) : DataItem() {
//        override val id = folder.folderId
//    }
//
//    data class PostItem(val post: Post) : DataItem() {
//        override val id: Long = post.id.toLong()
//    }
//
//    abstract val id: Long
//}
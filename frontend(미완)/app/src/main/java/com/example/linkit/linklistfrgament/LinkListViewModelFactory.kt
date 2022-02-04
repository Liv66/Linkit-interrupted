package com.example.linkit.linklistfrgament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.linkit.Repository
import com.example.linkit.database.Folder
import com.example.linkit.database.FolderAndLinkDao

class LinkListViewModelFactory(
    private val folderKey : Folder?,
    private val query:String?,
    private val repository: Repository,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LinkListViewModel::class.java)) {
            return LinkListViewModel(folderKey, query, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
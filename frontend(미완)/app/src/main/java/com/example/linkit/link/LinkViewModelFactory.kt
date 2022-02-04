package com.example.linkit.link

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.linkit.Repository
import com.example.linkit.database.Link



class LinkViewModelFactory(
    private val linkKey : Link,
    private val repository: Repository,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LinkViewModel::class.java)) {
            return LinkViewModel(linkKey, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
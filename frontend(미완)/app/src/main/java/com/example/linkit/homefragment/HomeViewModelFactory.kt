package com.example.linkit.homefragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.linkit.Repository

class HomeViewModelFactory (
    private val repository: Repository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
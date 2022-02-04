package com.example.linkit.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.linkit.R
import com.example.linkit.Repository
import com.example.linkit.database.FolderDatabase
import com.example.linkit.databinding.FragmentTestBinding
import kotlinx.coroutines.runBlocking

class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding: FragmentTestBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        // Inflate the layout for this fragment

        val application = requireNotNull(this.activity).application
        val dataSource = FolderDatabase.getInstance(application).folderDao()


        val repository = Repository(dataSource)
        val viewModelFactory = TestViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(TestViewModel::class.java)





        return binding.root
    }



}
package com.example.linkit.link

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.linkit.R
import com.example.linkit.Repository
import com.example.linkit.database.FolderAndLinkDao
import com.example.linkit.database.FolderDatabase
import com.example.linkit.database.Link
import com.example.linkit.databinding.FragmentLinkBinding

class LinkFragment : Fragment() {

    private lateinit var binding: FragmentLinkBinding
    private lateinit var repository: Repository
    private lateinit var link: Link
    private lateinit var arguments: LinkFragmentArgs
    private lateinit var application: Application
    private lateinit var dataSource: FolderAndLinkDao
    private lateinit var viewModelFactory : LinkViewModelFactory
    private lateinit var linkViewModel: LinkViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_link, container, false)

        arguments = LinkFragmentArgs.fromBundle(requireArguments())
        link = arguments.linkKey
        application = requireNotNull(this.activity).application

        dataSource = FolderDatabase.getInstance(application).folderDao()
        repository = Repository(dataSource)

        viewModelFactory = LinkViewModelFactory(link, repository)

        linkViewModel =
            ViewModelProvider(this, viewModelFactory).get(LinkViewModel::class.java)

        binding.lifecycleOwner = this
        binding.ll = linkViewModel
        binding.urlTv.text = link.url
        binding.tagTv.setText(link.tags)
        binding.memoEt.setText(link.memo)

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        link.memo = binding.memoEt.text.toString()
        link.tags = binding.tagTv.text.toString()
        linkViewModel.updateLink(link)
    }
}


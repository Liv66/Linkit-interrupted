package com.example.linkit.linklistfrgament

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkit.R
import com.example.linkit.Repository
import com.example.linkit.database.FolderAndLinkDao
import com.example.linkit.database.FolderDatabase
import com.example.linkit.databinding.FragmentLinkListBinding
import kotlinx.coroutines.runBlocking

class LinkListFragment : Fragment() {

    private lateinit var viewModelFactory: LinkListViewModelFactory
    private lateinit var linkListViewModel: LinkListViewModel
    private lateinit var binding: FragmentLinkListBinding
    private lateinit var application: Application
    private lateinit var arguments: LinkListFragmentArgs
    private lateinit var databaseDao: FolderAndLinkDao
    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_link_list, container, false)

        application = requireNotNull(this.activity).application
        arguments = LinkListFragmentArgs.fromBundle(requireArguments())

        databaseDao = FolderDatabase.getInstance(application).folderDao()
        repository = Repository(databaseDao)

        viewModelFactory =
            LinkListViewModelFactory(arguments.folder, arguments.query, repository)

        linkListViewModel =
            ViewModelProvider(this, viewModelFactory).get(LinkListViewModel::class.java)

        binding.linkListViewModel = linkListViewModel

        binding.lifecycleOwner = this

        val adapter = LinkListAdapter(LinkListener { linkListViewModel.onLinkClicked(it) })
        binding.linkListRv.adapter = adapter
        binding.linkListRv.layoutManager = LinearLayoutManager(activity)

        linkListViewModel.links.observe(viewLifecycleOwner, {
            it?.let {
                Log.d("##12", "$it")
                adapter.addAndSubmitList(it)
            }
        })

        linkListViewModel.navigateToLink.observe(viewLifecycleOwner, {
            it?.let {
                this.findNavController().navigate(
                    LinkListFragmentDirections.actionLinkListFragmentToLinkFragment(it)
                )
                linkListViewModel.onLinkNavigated()
            }
        })

        binding.addBtn.setOnClickListener {
            if (arguments.folder!!.share) {
                Log.d("@@@@@@@","쉐어됨")
                runBlocking {
                    linkListViewModel.insertLink_rd(
                        "rlawjdgjs000",
                        arguments.folder!!,
                        binding.etLinkName.text.toString()
                    )
                }
            } else {
                linkListViewModel.onAdd(
                    binding.etLinkName.text.toString(),
                    arguments.folder!!.key
                )
            }
            binding.etLinkName.setText(" ")
        }

        binding.makeShareBtn.setOnClickListener {
            linkListViewModel.transFolder("rlawjdgjs00", "das", arguments.folder!!,)
            it.visibility = View.INVISIBLE
        }




        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.link_list_menu, menu)
        menu.findItem(R.id.accountFragment).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}
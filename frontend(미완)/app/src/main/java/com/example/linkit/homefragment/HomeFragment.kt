package com.example.linkit.homefragment

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.linkit.R
import com.example.linkit.Repository
import com.example.linkit.SlideUpDialog
import com.example.linkit.database.FolderAndLinkDao
import com.example.linkit.database.FolderDatabase
import com.example.linkit.databinding.FolderDialogBinding
import com.example.linkit.databinding.FragmentHomeBinding
import com.example.linkit.objects.Account
import java.lang.Exception

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var application: Application
    private lateinit var databaseDao: FolderAndLinkDao
    private lateinit var repository:Repository
    private lateinit var viewModelFactory:HomeViewModelFactory
    private lateinit var homeViewModel:HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        application = requireNotNull(this.activity).application
        databaseDao = FolderDatabase.getInstance(application).folderDao()
        repository = Repository(databaseDao)

        viewModelFactory = HomeViewModelFactory(repository, application)
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.navigateToLinkList.observe(viewLifecycleOwner, {
            it?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToLinkListFragment(it, null))
                homeViewModel.onFolderNavigated()
            }
        })

        homeViewModel.folderName.observe(viewLifecycleOwner, {
            if (it != null) {
                homeViewModel.onDelete(it)
            }
        })

        fun fdsa(){
            var contentView: View = (activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.popup_slide, null)
            val slideupPopup = SlideUpDialog.Builder(context)
                .setContentView(contentView)
                .create()

            contentView.findViewById<Button>(R.id.close).setOnClickListener {
                slideupPopup.dismissAnim()
            }

            slideupPopup.show()
        }

        val manager = GridLayoutManager(activity, 3)
        binding.folderRv.layoutManager = manager

        val adapter = HomeAdapter(FolderListener {
            homeViewModel.onFolderClicked(it)
        }, FolderListener2 {
            homeViewModel.onFolderNameClicked(it)
        })

        binding.folderRv.adapter = adapter

        homeViewModel.folders.observe(viewLifecycleOwner, {
            it?.let {
                adapter.addAndSubmitList(it)
            }
        })

        binding.folderAddBtn.setOnClickListener {
//            makeDialog(requireContext(), homeViewModel)
            fdsa()
        }


        return binding.root
    }

    private fun makeDialog(context: Context, homeViewModel: HomeViewModel) {
        try {

            val dialogBinding = FolderDialogBinding.inflate(layoutInflater)
            val items = arrayOf("개인 폴더", "공유 폴더")
            var sItem = items[0]

            val builder = AlertDialog.Builder(context)
            builder.setTitle("커스텀 다이얼로그")
                .setIcon(R.mipmap.ic_launcher)
                .setView(dialogBinding.root)
                .setCancelable(true)
                .setSingleChoiceItems(items, 0) { _, which ->
                    sItem = items[which]
                }

            // p0에 해당 AlertDialog가 들어온다. findViewById를 통해 view를 가져와서 사용
            val listener = DialogInterface.OnClickListener { p0, _ ->
                val alert = p0 as AlertDialog
                val edit1: EditText? = alert.findViewById(R.id.folder_name_et)
                if (sItem == "개인 폴더") {
                    homeViewModel.onAdd(edit1?.text.toString())
                } else {
                    //계정 email
                    homeViewModel.insertFolder_rd("rlawjdgjs000",edit1?.text.toString())
                }
            }

            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", null)

            builder.show()
        } catch (e: Exception) {
            Log.d("12", "$e")
        }
    }

}

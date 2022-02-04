package com.example.linkit.accountfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.linkit.MainActivity
import com.example.linkit.R
import com.example.linkit.databinding.FragmentAccountBinding
import com.example.linkit.objects.Account


class AccountFragment : Fragment() {
    lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_account, container, false
        )
        val accountViewModel = AccountViewModel()
        binding.viewModel = accountViewModel
        binding.lifecycleOwner = this
        binding.button4.text = if (Account.toggle) "Push 알림 on" else "Push 알림 off"

        binding.button4.setOnClickListener {
            if (Account.toggle) {
                Account.toggle = false
                binding.button4.text = "Push 알림 off"
                Log.d("#####","tt ${Account.toggle}")
                (activity as MainActivity).setAlarm()
            } else {
                Account.toggle = true
                Log.d("#####","zz ${Account.toggle}")
                binding.button4.text = "Push 알림 on"
                (activity as MainActivity).setAlarm()
            }
        }
        binding.tvProfileName.setOnClickListener {
            goLoginScreen()
        }
        return binding.root
    }

    private fun goLoginScreen() {
        if (Account.isGuest) {
            findNavController().navigate(
                AccountFragmentDirections.actionAccountFragmentToLoginFragment()
            )
        }
    }

}
package com.example.linkit.accountfragment

import androidx.lifecycle.ViewModel
import com.example.linkit.objects.Account

class AccountViewModel:ViewModel() {
    val emailText: String
        get() = Account.email ?: "로그인 하러 가기"
}
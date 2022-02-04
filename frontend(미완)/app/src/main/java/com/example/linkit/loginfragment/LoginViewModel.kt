package com.example.linkit.loginfragment

import androidx.lifecycle.ViewModel
import com.example.linkit.RetrofitInstance
import com.example.linkit.network.SNSToken
import com.example.linkit.network.UserData
import com.example.linkit.objects.Account
import com.example.linkit.objects.Server
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.lang.IllegalArgumentException

class LoginViewModel() : ViewModel() {
    val gao: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(Server.clientID)
        .requestEmail()
        .build()

    fun setUserData(user: UserData?) {
        if (user!=null) {
            Account.email = user.email
            Account.token = user.access_token
            Account.name = user.name
        } else {
            Account.isGuest = true
        }
    }

    fun getGoogleIdToken(task: Task<GoogleSignInAccount>): String? {
        val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
        return account.idToken
    }

    suspend fun getGoogleUser(token: String): UserData? {
        val user = RetrofitInstance.api.gLogin(SNSToken(token))
        if (user.code() != 200)
            throw IllegalArgumentException(user.body()?.msg ?: "")

        return user.body()?.res_data
    }
}
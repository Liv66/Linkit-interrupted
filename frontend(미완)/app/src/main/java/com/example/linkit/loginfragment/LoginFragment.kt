package com.example.linkit.loginfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.linkit.databinding.FragmentLoginBinding
import com.example.linkit.network.UserData
import com.example.linkit.objects.Account
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException


class LoginFragment : Fragment() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var viewBinding: FragmentLoginBinding
    lateinit var viewModel: LoginViewModel
    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        CoroutineScope(Dispatchers.Default).launch {
            handleGoogleLogin(task)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 뷰 바인딩
        viewBinding = FragmentLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // 구글 로그인 객체
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), viewModel.gao)

        // 구글 로그인 버튼
        val gLoginBtn: SignInButton = viewBinding.btnGoogleLogin
        customizeGoogleButton(gLoginBtn)

        // 게스트 로그인
        val loginSkipTV: TextView = viewBinding.tvLoginSkip
        loginSkipTV.setOnClickListener {
            viewModel.setUserData(null)
            goHomeScreen()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return viewBinding.root
    }

    // 구글 로그인 버튼에 스타일, 동작 할당
    private fun customizeGoogleButton(btn: SignInButton) {
        // 구글 로그인 팝업 띄우기
        fun signInGoogle() {
            val signInIntent = mGoogleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }
        // 텍스트 바꾸기
        fun setText(btn: SignInButton, text: String) {
            // 자식 컴포넌트 중 TextView 찾기
            for (i in 0 until btn.childCount) {
                val v: View = btn.getChildAt(i)
                println(v.javaClass.name)
                if (v is TextView) {
                    v.setText(text)
                    return
                }
            }
        }

        btn.setOnClickListener { signInGoogle() }
        btn.setSize(SignInButton.SIZE_WIDE)
        setText(btn, "Google 계정으로 로그인")
    }

    // 계정정보 저장 후 화면 이동
    private suspend fun handleGoogleLogin(task: Task<GoogleSignInAccount>) {
        try {
            val token = viewModel.getGoogleIdToken(task)
            val user = viewModel.getGoogleUser(token!!)

            // 유저정보 저장 후 다음 페이지 이동
            viewModel.setUserData(user)
            goHomeScreen()
        } catch (e: ApiException) {
            makeToast("통신 오류: " + e.statusCode)
        } catch (e: ConnectException) {
            makeToast("서버 오류")
        }
    }

    private fun makeToast(msg: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(requireContext(),msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun goHomeScreen() {
        // 다음 페이지로 이동
        lifecycleScope.launch(Dispatchers.Main) {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            )
        }
    }
}
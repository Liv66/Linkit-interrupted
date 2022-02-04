package com.example.linkit.objects

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit

// 앱 전체에서 로그인 정보를 제공한다.
// MainActivity에서 init 실행할 것!
object Account {
    private val PREF_KEY = "autoLogin"
    private val PREF_EMAIL = "email"
    private val PREF_TOKEN = "token"
    private val PREF_NAME = "name"
    private val PREF_TOGGLE = "toggle"
    private val PREF_GUEST = "guest"
    lateinit var sharedPref: SharedPreferences
    var email: String?
        get() = sharedPref.getString(PREF_EMAIL, null)
        set(value) = setUserData(PREF_EMAIL, value)
    var token: String?
        get() = sharedPref.getString(PREF_TOKEN, null)
        set(value) = setUserData(PREF_TOKEN, value)
    var name: String?
        get() = sharedPref.getString(PREF_NAME, null)
        set(value) = setUserData(PREF_NAME, value)
    var isGuest: Boolean
        get() = sharedPref.getBoolean(PREF_GUEST, false)
        set(value) = sharedPref.edit().putBoolean(PREF_GUEST, value).apply()
    var toggle: Boolean
        get() = sharedPref.getBoolean(PREF_TOGGLE, true)
        set(value) = sharedPref.edit().putBoolean(PREF_TOGGLE, value).apply()

    fun init(ctx: Context) {
        sharedPref = ctx.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(): Boolean {
        return email != null || isGuest
    }

    fun logoutAccount() {
        email = null
        name = null
        token = null
        isGuest = false
    }

    fun setUserData(key: String, value: String?) {
        assert(this::sharedPref.isInitialized)

        sharedPref.edit().putString(key, value).apply()
        isGuest = false
    }
}
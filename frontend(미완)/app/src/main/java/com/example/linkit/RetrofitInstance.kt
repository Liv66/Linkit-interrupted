package com.example.linkit

import com.example.linkit.network.SimpleApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    const val URL = "https://link-manage-apptive.herokuapp.com"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java)
    }
}

//"http://192.168.219.100:5000/ "
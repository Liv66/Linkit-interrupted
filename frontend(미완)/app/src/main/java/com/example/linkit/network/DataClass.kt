package com.example.linkit.network

import com.google.gson.annotations.SerializedName

// TODO: Generics로 통일
data class BaseResponse<T>(
    val success: Boolean,
    val status: Int,
    val msg: String,
    val res_data:T,
)

data class Post(
    @SerializedName("userId")
    val myUserId: Int,
    val id: Int,
    val title: String,
    val body: String,
)

//insert 클래스
data class Insert(
    val meta: Meta,
    val data: List<Data>,
)

data class Meta(
    val gid: Long? = null,
    val type:Int? = null,
    val owner: String? = null,
    val snode: Long? = null,
)

data class Data(
    val type: Int,
    val content: Content,
)

data class Content(
    val name: String?,
    val url:String?,
    val tags: List<String>?,
    val memo: String?,
    val img:String?
)

// insert response
data class ResInsert(
    val success: Boolean,
    val status: Int,
    val msg: String,
    val res_data:ResData,
)

data class ResData(
    val gid: Long,
    val snodes: List<Long>,
)

// Get 클래스
data class Snode(
    val snode:Long,
)

data class Email(
    val email:String
)

// Get response
data class ResGet(
    val success: Boolean,
    val status: Int,
    val msg: String,
    val res_data:List<ResGetData>,
)

data class ResGetData(
    val meta:Meta,
    val content:Content
)

// delete 클래스
data class Snodes(
    val snodes:List<Long>
)

data class SNSToken(
    val token: String
)

data class UserData(
    val access_token: String,
    val email: String,
    val name: String
)

// 공유사용자 추가
data class Add(
    val gid:Long,
    val email:List<String>
)
package com.example.linkit

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.linkit.database.Folder
import com.example.linkit.database.FolderAndLinkDao
import com.example.linkit.database.Link
import com.example.linkit.network.*
import retrofit2.Response

class Repository(private val ff:FolderAndLinkDao) {

    fun getAllFolder(): LiveData<List<Folder>>{
        return ff.getAllFolder()
    }

    suspend fun insertFolder(folder:Folder){
        ff.insertFolder(folder)
    }

    suspend fun updateFolder(folder:Folder){
        ff.updateFolder(folder)
    }


    suspend fun clearFolder(){
        ff.clearFolder()
    }


    fun getQueryLink(query:String):LiveData<List<Link>>{
        return ff.getQueryLink(query)
    }

    fun getAllLink(key:Long): LiveData<List<Link>>{
        return ff.getAllLink(key)
    }

    suspend fun insertLink(link: Link){
        ff.insertLink(link)
    }

    suspend fun updateLink(link: Link){
        ff.updateLink(link)
    }

    suspend fun clearLink(){
        ff.clearLink()
    }



    suspend fun getLink(key:Long?): Link? {
        val aa = ff.getLink(key)
        Log.d("##45","${aa}")
        return aa
    }

    suspend fun getFolder(key: Long?): Folder?{
        val aa = ff.getFolder(key)
        return aa
    }

    suspend fun deleteFolder(id:Folder){
        ff.deleteFolder(id)
        ff.deletefl(id.key)
        if(id.share){
            RetrofitInstance.api.delete(Snodes(listOf(id.snodes!!.toLong())))
        }
    }


    suspend fun rd_insert(insert: Insert):Response<ResInsert>{
        return RetrofitInstance.api.insert(insert)
    }

    suspend fun rd_get(snodes:Snode):Response<ResGet>{
        return RetrofitInstance.api.get(snodes)
    }

    suspend fun rd_get(email:Email):Response<ResGet>{
        return RetrofitInstance.api.get(email)
    }

    suspend fun rd_add(add:Add){
        return RetrofitInstance.api.add(add)
    }

    suspend fun loginGoogle(token: SNSToken): Response<BaseResponse<UserData>> {
        return RetrofitInstance.api.gLogin(token)
    }
}
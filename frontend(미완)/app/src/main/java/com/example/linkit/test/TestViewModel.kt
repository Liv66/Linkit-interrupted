package com.example.linkit.test

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.linkit.Repository
import com.example.linkit.database.Folder
import com.example.linkit.database.Link
import com.example.linkit.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TestViewModel(private val repository: Repository) : ViewModel() {

    var aa: Link? = Link()
    var qq: Folder? = Folder()

    suspend fun deleteFolder() {

    }

//   suspend fun insertLink(email: String, url: String, tags:String,memo:String){
//       //공유링크의 gid, snode는 누구의 것?
//       val meta = Meta(owner = email,gid =  )
//       val tagList = tags.split(" ")
//       val content = Content(url = url, tags=tagList,memo = memo,name = null,img = null )
//       val data = Data(1, content)
//       val insert = Insert(meta, listOf(data))
//       val res = repository.rd_insert(insert)
//       repository.insertLink(Link(url=url, tags = tags, memo = memo, snodes = res.body()?.res_data?.sndoes, gid = res.body()?.res_data!!.gid, parent =  ??))
//   }

    suspend fun insertFolder(email: String, name: String) {
        val meta = Meta(owner = email) // email은 싱글톤
        val content = Content(name = name, null, null, null, null)
        val data = Data(2, content)
        val insert = Insert(meta, listOf(data))
        val res = repository.rd_insert(insert)
//        repository.insertFolder(Folder(name = name, snodes = res.body()?.res_data?.snodes))
    }

    suspend fun get(email: String) {

        //처음 로딩시 or 새로 고침시 호출할 함수

        try {
            val res = repository.rd_get(Email(email))
            res.body()?.res_data?.forEach {
                if (it.meta.type == 1) {
                    getLink(it)
                } else {
                    getFolder(it)
                }
            }
        } catch (e: Exception) {
            Log.d("@@12", "$e")
        }

    }

    suspend fun getFolder(dd: ResGetData) {
        val job = viewModelScope.launch(Dispatchers.IO) {
            qq = repository.getFolder(dd.meta.snode)
            if (qq == null) {
                var folder = Folder(name = dd.content.name)
                repository.insertFolder(folder)
            } else {
                qq!!.name = dd.content.name
                repository.updateFolder(qq!!)
            }
        }
    }

    suspend fun getLink(dd: ResGetData) {
        viewModelScope.launch(Dispatchers.IO) {
            aa = repository.getLink(dd.meta.snode)
            var tagString = ""
            dd.content.tags?.forEach {
                tagString = tagString.plus(" $it")
            }
            if (aa == null) {
                val link = Link(url = dd.content.url, memo = dd.content.memo, tags = tagString)
                repository.insertLink(link)
            } else {
                aa!!.url = dd.content.url
                aa!!.memo = dd.content.memo
                aa!!.tags = tagString
                repository.updateLink(aa!!)
            }
        }
    }

}

class TestViewModelFactory(
    private val repository: Repository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TestViewModel(repository) as T
    }
}

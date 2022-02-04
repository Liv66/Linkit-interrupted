package com.example.linkit.linklistfrgament

import android.util.Log
import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkit.Repository
import com.example.linkit.database.Folder
import com.example.linkit.database.Link
import com.example.linkit.network.Content
import com.example.linkit.network.Data
import com.example.linkit.network.Insert
import com.example.linkit.network.Meta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LinkListViewModel(val folder: Folder?, query: String?, private val repository: Repository) :
    ViewModel() {

    private var link = MutableLiveData<Link?>()

    init {
        Log.d("@@@@@@@","$folder")
    }

    val links =
        if (folder == null) repository.getQueryLink(query!!
        ) else repository.getAllLink(folder.key)


    private val _navigateToLink = MutableLiveData<Link?>()
    val navigateToLink
        get() = _navigateToLink

    fun onLinkClicked(id: Link) {
        _navigateToLink.value = id
    }

    fun onLinkNavigated() {
        _navigateToLink.value = null
    }

    fun onAdd(url: String, parent: Long) {

        viewModelScope.launch {
            val newLink = Link()
            newLink.url = url
            newLink.parent = parent
            insert(newLink)
            Log.d("@@@@@@@@","${links.value}")
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            repository.clearLink()
        }
    }

    suspend fun insertLink_rd(email: String, folder: Folder, url: String) {
        //공유폴더에서 링크 추가하는 경우
        viewModelScope.launch {
            val meta = Meta(owner = email, gid = folder.gid, snode = folder.snodes)
            val content = Content(url = url, memo = null, name = null, img = null, tags = null)
            val data = Data(1, content)
            val insert = Insert(meta, listOf(data))
            Log.d("!!3333", "${insert}")
            val res = repository.rd_insert(insert)

            val newLink = Link(url = url,
                snodes = res.body()?.res_data?.snodes?.get(0),
                gid = res.body()?.res_data?.gid,
                parent = folder.key,
                share = true)
            insert(newLink)
            Log.d("@@@@@@@@","${links.value}")
        }
    }

    fun transFolder(email: String, name: String, folder: Folder){

        val meta = Meta(owner = email) // email은 싱글톤
        val content = Content(name = name, null, null, null, null)
        val data = Data(2, content)
        val ins = Insert(meta, listOf(data))
        viewModelScope.launch {
            val res = repository.rd_insert(ins)
            folder.share = true
            folder.snodes = res.body()?.res_data?.snodes?.get(0)
            folder.gid = res.body()?.res_data?.gid
            repository.updateFolder(folder)
            links.value?.forEach {
                val tagList = it.tags?.split(" ")
                val metaL = Meta(owner = email, gid = folder.gid, snode = folder.snodes)
                val contentL = Content(url = it.url, memo = it.memo, name = it.name, img = null, tags = tagList)
                val dataL = Data(1, contentL)
                val insertL = Insert(metaL, listOf(dataL))
                val resL = repository.rd_insert(insertL)
                it.share = true
                it.snodes = resL.body()?.res_data?.snodes?.get(0)
                it.gid = resL.body()?.res_data!!.gid
                it.parent = folder.key
                update(it)
            }
        }

    }

    private suspend fun update(link: Link) {
        withContext(Dispatchers.IO) {
            repository.updateLink(link)
        }
    }

    private suspend fun insert(link: Link) {
        withContext(Dispatchers.IO) {
            repository.insertLink(link)
        }
    }

    fun onClear() {
        viewModelScope.launch {
            // Clear the database table.
            clear()
            // And clear tonight since it's no longer in the database
            link.value = null
        }
    }
}

@BindingAdapter("makeSharedFolder")
fun makeShareFolder(button:Button, folder:Folder) {
    Log.d("55555555","${folder}")
    if(!folder.share){
        button.visibility = View.VISIBLE
    }else{
        button.visibility = View.INVISIBLE
    }
}
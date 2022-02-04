package com.example.linkit.homefragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkit.Repository
import com.example.linkit.database.Folder
import com.example.linkit.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val repository: Repository,
    application: Application,
) :
    AndroidViewModel(application) {

    val folders = repository.getAllFolder()

    private val _navigateToLinklist = MutableLiveData<Folder?>()
    val navigateToLinkList
        get() = _navigateToLinklist

    private val _folderName = MutableLiveData<Folder?>() // 폴더 이름 누르면 해당 폴더 삭제되게 해놓음, 임시로 설정한 기능
    val folderName
        get() = _folderName


    fun onFolderNameClicked(id:Folder){
        _folderName.value = id
    }

    fun onFolderClicked(id: Folder) {
        _navigateToLinklist.value = id
    }

    fun onFolderNavigated() {
        _navigateToLinklist.value = null
    }

    fun onAdd(name: String) {
        viewModelScope.launch {
            val newFolder = Folder()
            newFolder.name = name
            insert(newFolder)
        }
    }

    fun insertFolder_rd(email: String, name: String) {
        val meta = Meta(owner = email) // email은 싱글톤
        val content = Content(name = name, null, null, null, null)
        val data = Data(2, content)
        val ins = Insert(meta, listOf(data))
        viewModelScope.launch {
            val res = repository.rd_insert(ins)
            val newFolder = Folder(name = name, snodes = res.body()?.res_data?.snodes?.get(0),gid = res.body()?.res_data?.gid, share = true)
            insert(newFolder)
            Log.d("@@@@@@@@","폴더 ${folders.value}")
        }
    }


    private suspend fun update(folder: Folder, name:String) {
        withContext(Dispatchers.IO) {
            folder.name = name
            repository.updateFolder(folder)
        }
    }

    private suspend fun insert(folder: Folder) {
        withContext(Dispatchers.IO) {
            repository.insertFolder(folder)
        }
    }

    private suspend fun delete(id: Folder){
        withContext(Dispatchers.IO){
            repository.deleteFolder(id)
        }
    }

    fun onDelete(id:Folder){
        viewModelScope.launch {
            delete(id)
        }
    }
}

package com.example.linkit.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import retrofit2.http.DELETE

@Dao
interface FolderAndLinkDao {

    @Query("SELECT * FROM folder_table WHERE `key` = :key")
    suspend fun getFolder(key: Long?): Folder

    @Query("SELECT * FROM folder_table ORDER BY `key` DESC")
    fun getAllFolder(): LiveData<List<Folder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: Folder)

    @Update
    suspend fun updateFolder(folder: Folder)

    @Query("DELETE FROM folder_table")
    suspend fun clearFolder()

    @Delete
    suspend fun deleteFolder(folder: Folder)

    @Query("DELETE FROM link_table WHERE parent = :key")
    suspend fun deletefl(key: Long?)


    @Query("SELECT * FROM link_table WHERE parent = :key ORDER BY `key` DESC")
    fun getAllLink(key: Long): LiveData<List<Link>>

    @Query("SELECT * FROM link_table WHERE tags LIKE :query || '%' ORDER BY `key` DESC ")
    fun getQueryLink(query: String): LiveData<List<Link>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(link: Link)

    @Update
    suspend fun updateLink(link: Link)

    @Query("DELETE FROM link_table")
    suspend fun clearLink()

    @Query("SELECT * FROM link_table WHERE `key` = :key")
    suspend fun getLink(key: Long?): Link

    @Query("SELECT `key` FROM link_table")
    suspend fun getAllKey(): List<Long>

    @Delete
    fun deleteLink(link: Link)

    @Query("DELETE FROM link_table")
    fun deleteAllLink()

}


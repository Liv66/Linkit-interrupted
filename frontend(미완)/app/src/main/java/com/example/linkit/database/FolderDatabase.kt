package com.example.linkit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Folder::class, Link::class], version = 1,exportSchema = false)
abstract class FolderDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderAndLinkDao

    companion object {
        @Volatile
        private var INSTANCE: FolderDatabase? = null

        fun getInstance(
            context: Context
        ): FolderDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FolderDatabase::class.java,
                    "folder_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
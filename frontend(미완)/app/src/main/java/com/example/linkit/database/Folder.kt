package com.example.linkit.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "folder_table")
@Parcelize
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val key: Long = 0L,

    @ColumnInfo
    var gid: Long? = null,

    @ColumnInfo
    var snodes: Long? = null,

    @ColumnInfo
    var name: String? = null,

    @ColumnInfo
    val img: String? = null,

    @ColumnInfo
    var share: Boolean = false,
):Parcelable

@Entity(tableName = "link_table")
@Parcelize
data class Link(
    @PrimaryKey(autoGenerate = true)
    val key: Long = 0L,

    @ColumnInfo
    var snodes: Long? = null,

    @ColumnInfo
    var gid: Long? = null,

    @ColumnInfo
    var parent: Long = 0L,

    @ColumnInfo
    var name: String? = null,

    @ColumnInfo
    var url: String? = null,

    @ColumnInfo
    var tags: String? = null,

    @ColumnInfo
    var memo: String? = null,

    @ColumnInfo
    var img: String? = null,

    @ColumnInfo
    var share: Boolean = false,

    ) : Parcelable


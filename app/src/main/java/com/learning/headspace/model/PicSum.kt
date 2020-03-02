package com.learning.headspace.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PicSum(@PrimaryKey val id : Int,
                  @ColumnInfo(name = "author") val author : String,
                  @ColumnInfo(name = "width") val width : Int,
                  @ColumnInfo(name = "height") val height : Int,
                  @ColumnInfo(name = "url") val url : String,
                  @ColumnInfo(name = "download_url") val download_url : String)
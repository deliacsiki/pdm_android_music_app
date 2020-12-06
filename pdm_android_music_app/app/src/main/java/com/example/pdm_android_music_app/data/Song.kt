package com.example.pdm_android_music_app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//data class Song (
//    val id: String,
//    var name: String,
//    var artist: String,
//    var releaseDate: String,
//    var downloaded: String
//) {
//    override fun toString(): String {
//        return "Song name: $name"
//    }
//}
@Entity(tableName = "songs")
data class Song(
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "artist") var artist: String,
    @ColumnInfo(name = "releaseDate") var releaseDate: String,
    @ColumnInfo(name = "downloaded") var downloaded: String,
    @ColumnInfo(name = "userId") var userId: String
) {
    override fun toString(): String = "$name by $artist"
}
package com.example.pdm_android_music_app.data


data class Song (
    val id: String,
    var name: String,
    var artist: String,
    var releaseDate: String,
    var downloaded: String
) {
    override fun toString(): String {
        return "'$name', '$artist', '$releaseDate', '$downloaded"
    }
}
package com.example.pdm_android_music_app.data

data class MessageData(var event: String, var payload: SongJson) {
    data class SongJson(var song: Song)
}
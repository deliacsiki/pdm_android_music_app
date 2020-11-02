package com.example.pdm_android_music_app.data

import android.util.Log
import com.example.pdm_android_music_app.core.TAG

object SongRepository {
    private var cachedItems: MutableList<Song>? = null;

    suspend fun loadAll(): List<Song> {
        Log.i(TAG, "loadAll")
        if (cachedItems != null) {
            return cachedItems as List<Song>;
        }
        cachedItems = mutableListOf()
        val items = SongApi.service.find()
        cachedItems?.addAll(items)
        return cachedItems as List<Song>
    }

    suspend fun load(itemId: String): Song {
        Log.i(TAG, "load")
        val item = cachedItems?.find { it.id == itemId }
        if (item != null) {
            return item
        }
        return SongApi.service.read(itemId)
    }

    suspend fun save(item: Song): Song {
        Log.i(TAG, "save")
        val createdItem = SongApi.service.create(item)
        cachedItems?.add(createdItem)
        return createdItem
    }

    suspend fun update(item: Song): Song {
        Log.i(TAG, "update")
        val updatedItem = SongApi.service.update(item.id, item)
        val index = cachedItems?.indexOfFirst { it.id == item.id }
        if (index != null) {
            cachedItems?.set(index, updatedItem)
        }
        return updatedItem
    }
}
package com.example.pdm_android_music_app.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.pdm_android_music_app.core.Constants
import com.example.pdm_android_music_app.core.Result
import com.example.pdm_android_music_app.data.local.SongDao
import com.example.pdm_android_music_app.data.remote.SongApi

class SongRepository(private val songDao: SongDao) {
    val songs = MediatorLiveData<List<Song>>().apply { postValue(emptyList()) }

    suspend fun refresh(): Result<Boolean> {
        try {
            val songsApi = SongApi.service.find()
            songs.value = songsApi
            for (flight in songsApi) {
                //plant.userId = Constants.instance()?.fetchValueString("_id")!!
                songDao.insert(flight)
            }
            return Result.Success(true)
        } catch (e: Exception) { // handle offline mode

            val userId = Constants.instance()?.fetchValueString("_id")
            songs.addSource(songDao.getAll(userId!!)) {
                songs.value = it
            }

            return Result.Error(e)
        }
    }

    fun getById(itemId: String): LiveData<Song> {
        return songDao.getById(itemId)
    }

    suspend fun save(item: Song): Result<Song> {
        try {
            val createdItem = SongApi.service.create(item)
            songDao.insert(createdItem)
            return Result.Success(createdItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(item: Song): Result<Song> {
        try {
            val updatedItem = SongApi.service.update(item._id, item)
            songDao.update(updatedItem)
            return Result.Success(updatedItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun delete(itemId: String): Result<Boolean> {
        try {
            SongApi.service.delete(itemId)
            songDao.delete(id = itemId)
            return Result.Success(true)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}
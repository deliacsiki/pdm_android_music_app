package com.example.pdm_android_music_app.item

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.pdm_android_music_app.core.TAG
import com.example.pdm_android_music_app.data.Song
import com.example.pdm_android_music_app.data.SongRepository
import com.example.pdm_android_music_app.data.local.SongDatabase
import com.example.pdm_android_music_app.core.Result

import kotlinx.coroutines.launch


class ItemEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableItem = MutableLiveData<Song>().apply { value = Song("", "", "", "", "", "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val item: LiveData<Song> = mutableItem
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val songRepository: SongRepository

    init {
        val songDao = SongDatabase.getDatabases(application, viewModelScope).songDao()
        songRepository = SongRepository(songDao)
    }

    fun loadItem(itemId: String): LiveData<Song> {
        return songRepository.getById(itemId)
    }

    fun saveOrUpdateItem(name: String, artist: String, releaseDate: String, downloaded: String) {
        viewModelScope.launch {
            Log.i(TAG, "saveOrUpdateItem...");
            val item = mutableItem.value ?: return@launch
            item.name = name
            item.artist = artist
            item.releaseDate = releaseDate
            item.downloaded = downloaded
            mutableFetching.value = true
            mutableException.value = null

            val result: Result<Song>

            if (item._id.isNotEmpty()) {
                result = songRepository.update(item)
            } else {
                result = songRepository.save(item)
            }

            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }

            Log.i(TAG, "saveOrUpdateItem succeeded");
            mutableCompleted.value = true
            mutableFetching.value = false

        }
    }
}
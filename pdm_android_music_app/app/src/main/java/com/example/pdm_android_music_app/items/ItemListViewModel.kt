package com.example.pdm_android_music_app.items


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pdm_android_music_app.core.TAG
import com.example.pdm_android_music_app.data.Song
import com.example.pdm_android_music_app.core.Result
import com.example.pdm_android_music_app.data.SongRepository
import com.example.pdm_android_music_app.data.local.SongDatabase
import kotlinx.coroutines.launch
import com.example.pdm_android_music_app.data.remote.SongApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class ItemListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableItems = MutableLiveData<List<Song>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    var items: LiveData<List<Song>> = mutableItems
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    private val songRepository: SongRepository

    init {
        val songDao = SongDatabase.getDatabases(application, viewModelScope).songDao()
        songRepository = SongRepository(songDao)
        items = songRepository.songs

        CoroutineScope(Dispatchers.Main).launch { collectEvents() }
    }

    suspend fun collectEvents() {
        while (true) {
            val event = SongApi.RemoteDataSource.eventChannel.receive()
            Log.d("ws", event)
            Log.d("MainActivity", "received $event")
            refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...")
            mutableLoading.value = true
            mutableException.value = null
            when (val result = songRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded")
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception)
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}
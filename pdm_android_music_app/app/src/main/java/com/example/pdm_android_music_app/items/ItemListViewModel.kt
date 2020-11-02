package com.example.pdm_android_music_app.items

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdm_android_music_app.core.TAG
import com.example.pdm_android_music_app.data.Song
import com.example.pdm_android_music_app.data.SongRepository
import kotlinx.coroutines.launch


class ItemListViewModel : ViewModel() {
    private val mutableItems = MutableLiveData<List<Song>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val items: LiveData<List<Song>> = mutableItems
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    fun createItem(position: Int): Unit {
        val list = mutableListOf<Song>()
        list.addAll(mutableItems.value!!)
        list.add(Song(position.toString(), "Song " + position, "", "", ""))
        mutableItems.value = list
    }

    fun loadItems() {
        viewModelScope.launch {
            Log.v(TAG, "loadItems...");
            mutableLoading.value = true
            mutableException.value = null
            try {
                mutableItems.value = SongRepository.loadAll()
                Log.d(TAG, "loadItems succeeded");
                mutableLoading.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadItems failed", e);
                mutableException.value = e
                mutableLoading.value = false
            }
        }
    }
}
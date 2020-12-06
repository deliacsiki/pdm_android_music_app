package com.example.pdm_android_music_app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.pdm_android_music_app.core.Constants
import com.example.pdm_android_music_app.core.TAG
import com.example.pdm_android_music_app.data.remote.SongApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    var isActive = false;

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(findViewById(R.id.toolbar))
        Constants.instance(this.applicationContext);
        Log.i(TAG, "onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item);
        }
    }

    override fun onStart() {
        super.onStart()
        isActive = true
        CoroutineScope(Dispatchers.Main).launch { collectEvents() }
    }

    override fun onStop() {
        super.onStop()
        isActive = false
    }

    private suspend fun collectEvents() {
        while (isActive) {
            val event = SongApi.RemoteDataSource.eventChannel.receive()
            Log.d("ws", event)
            Log.d("MainActivity", "received $event")
        }
    }
}
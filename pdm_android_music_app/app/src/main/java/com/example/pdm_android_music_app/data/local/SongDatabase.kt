package com.example.pdm_android_music_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pdm_android_music_app.data.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Song::class], version = 1)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getDatabases(context: Context, scope: CoroutineScope): SongDatabase {
            val inst = INSTANCE
            if (inst != null) {
                return inst
            }

            val instance = Room.databaseBuilder(
                context.applicationContext,
                SongDatabase::class.java,
                "song_db"
            ).addCallback(WordDatabaseCallback(scope))
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            return instance
        }

        private class WordDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        //populateDatabase(database.plantDao())
                    }
                }
            }
        }
    }
}

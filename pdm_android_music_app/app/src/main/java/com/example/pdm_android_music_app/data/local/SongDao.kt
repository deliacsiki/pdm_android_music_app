package com.example.pdm_android_music_app.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pdm_android_music_app.data.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM songs WHERE userId=:userId")
    fun getAll(userId: String): LiveData<List<Song>>

    @Query("SELECT * FROM songs WHERE _id=:id")
    fun getById(id: String): LiveData<Song>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flight: Song)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(flight: Song)

    @Query("DELETE FROM songs WHERE _id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM songs")
    suspend fun deleteAll()
}
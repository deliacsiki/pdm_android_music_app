package com.example.pdm_android_music_app.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object SongApi {
    private const val URL = "http://192.168.1.6:3001/"

    interface Service {
        @GET("/song")
        suspend fun find(): List<Song>

        @GET("/song/{id}")
        suspend fun read(@Path("id") itemId: String): Song;

        @Headers("Content-Type: application/json")
        @POST("/song")
        suspend fun create(@Body item: Song): Song

        @Headers("Content-Type: application/json")
        @PUT("/song/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Song): Song
    }

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val service: Service = retrofit.create(Service::class.java)
}
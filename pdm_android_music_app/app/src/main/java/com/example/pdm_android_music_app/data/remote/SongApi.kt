package com.example.pdm_android_music_app.data.remote

import android.util.Log
import com.example.pdm_android_music_app.core.Api
import com.example.pdm_android_music_app.core.Constants
import com.example.pdm_android_music_app.data.Song
import com.google.gson.GsonBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object SongApi {
//    private const val URL = "http://192.168.0.132:3001/"
    private const val WSURL = "http://192.168.0.132:3001" // "ws://192.168.0.150:3000"

    interface Service {
        @GET("/api/song/")
        suspend fun find(): List<Song>

        @GET("/api/song/{id}")
        suspend fun read(@Path("id") itemId: String): Song;

        @Headers("Content-Type: application/json")
        @POST("/api/song/")
        suspend fun create(@Body item: Song): Song

        @Headers("Content-Type: application/json")
        @PUT("/api/song/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Song): Song

        @DELETE("/api/song/{id}")
        suspend fun delete(@Path("id") itemId: String): Response<Unit>
    }


    val service: Service = Api.retrofit.create(Service::class.java)

    object RemoteDataSource {
        val eventChannel = Channel<String>()

        init {
            val request = Request.Builder().url(WSURL).build()
            OkHttpClient().newWebSocket(request, MyWebSocketListener()).request()
        }

        private class MyWebSocketListener : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                Log.d("WebSocket", "onOpen")
                val token = Constants.instance()?.fetchValueString("token")
                val json =
                    "{\"type\":\"authorization\",\"payload\":{\"token\":\"$token\"}}"
                //"{\"type\":\"authorization\",\"payload\":{\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImIiLCJfaWQiOiJ2STFHTWt6QnphNWk4Mnp6IiwiaWF0IjoxNjA1NjI5MDU4LCJleHAiOjE2MDU4NDUwNTh9.GepIJPYh_qR-5nRNIULd--7cT5tdfhJhmzSQKTApVzA\"}}"
                Log.d("json", json)
                webSocket.send(json)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "onMessage$text")
                runBlocking { eventChannel.send(text) }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d("WebSocket", "onMessage bytes")
                output("Receiving bytes : " + bytes.hex())
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                Log.e("WebSocket", "onFailure", t)
                t.printStackTrace()
            }

            private fun output(txt: String) {
                Log.d("WebSocket", txt)
            }
        }
    }

}
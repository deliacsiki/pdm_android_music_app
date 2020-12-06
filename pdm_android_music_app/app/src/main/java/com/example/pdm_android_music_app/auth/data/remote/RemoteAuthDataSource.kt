package com.example.pdm_android_music_app.auth.data.remote

import com.example.pdm_android_music_app.auth.data.TokenHolder
import com.example.pdm_android_music_app.auth.data.User
import com.example.pdm_android_music_app.core.Api
import com.example.pdm_android_music_app.core.Result

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object RemoteAuthDataSource {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/auth/login")
        suspend fun login(@Body user: User): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(user: User): Result<TokenHolder> {
        try {
            return Result.Success(authService.login(user))
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}


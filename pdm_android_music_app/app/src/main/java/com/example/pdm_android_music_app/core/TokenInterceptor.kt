package com.example.pdm_android_music_app.core

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor constructor() : Interceptor {
  var token: String? = null

  override fun intercept(chain: Interceptor.Chain): Response {
    val original = chain.request()
    val originalUrl = original.url
    if (token == null) {
      token = Constants.instance()?.fetchValueString("token")
//      return chain.proceed(original)
    }
    val requestBuilder = original.newBuilder()
      .addHeader("Authorization", "Bearer $token")
      .url(originalUrl)
    val request = requestBuilder.build()
    return chain.proceed(request)
  }
}
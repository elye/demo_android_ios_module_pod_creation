package com.elyeproj.fetcher

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request

class Fetcher(userAgentInterceptor: Interceptor) {

    private val httpClient by lazy {
        OkHttpClient
            .Builder()
            .addInterceptor(userAgentInterceptor)
            .build()
    }

    fun fetchOnBackground(httpUrl: HttpUrl, onSuccess: (result: String?) -> String): String {
        val request = Request.Builder().get().url(httpUrl).build()
        val response = httpClient.newCall(request).execute()

        return if (response.isSuccessful) {
            val raw = response.body()?.string()
            onSuccess(raw)
        } else {
            response.message()
        }
    }
}
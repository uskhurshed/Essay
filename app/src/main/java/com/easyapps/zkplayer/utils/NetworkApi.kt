package com.easyapps.zkplayer.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class NetworkApi {

    suspend fun startRequest(): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://raw.githubusercontent.com/uskhurshed/apps/main/esse-new.json")
            .build()

        return@withContext try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) response.body?.string() ?: ""
            else ""
        } catch (e: IOException) {
            ""
        }
    }
}

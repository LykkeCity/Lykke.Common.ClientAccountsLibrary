package com.lykke.client.accounts.http.impl

import com.google.gson.Gson
import com.lykke.client.accounts.http.HttpClient
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class HttpCleintImpl : HttpClient {
    private companion object {
        val URL_PARAMS_SEPARATOR = "&"
    }

    override fun <T> get(url: URL,
                         params: Map<String, String?>,
                         responseClass: Class<T>,
                         connectTimeout: Int,
                         readTimeout: Int): T {
        val gson = Gson()

        val httpUrlConnection = url.openConnection() as HttpURLConnection
        httpUrlConnection.requestMethod = "GET"
        httpUrlConnection.addRequestProperty("Content-Type", "application/json")
        httpUrlConnection.doOutput = true

        httpUrlConnection.connectTimeout = connectTimeout
        httpUrlConnection.readTimeout = readTimeout

        val connectionOutputStream = DataOutputStream(httpUrlConnection.outputStream)
        connectionOutputStream.writeBytes(getRequestParams(params))
        connectionOutputStream.flush()
        connectionOutputStream.close()

        val result = StringBuilder()
        val responseReader = BufferedReader(InputStreamReader(httpUrlConnection.inputStream))
        var line = responseReader.readLine()

        while (line != null) {
            result.append(line)
            line = responseReader.readLine()
        }

        httpUrlConnection.disconnect()

        return gson.fromJson(responseReader.toString(), responseClass)
    }

    private fun getRequestParams(params: Map<String, String?>): String {
        return params
            .entries
            .asSequence()
            .filter { it.value != null }
            .map { URLEncoder.encode(it.key, "UTF-8") + "=" + URLEncoder.encode(it.value, "UTF-8") }
            .joinToString(URL_PARAMS_SEPARATOR)
    }
}
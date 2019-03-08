package com.lykke.client.accounts.http

import java.net.URL

interface HttpClient {
     fun <T> get(url: URL,
                 params: Map<String, String?>, responseClass: Class<T>,
                 connectTimeout: Int,
                 readTimeout: Int): T
}
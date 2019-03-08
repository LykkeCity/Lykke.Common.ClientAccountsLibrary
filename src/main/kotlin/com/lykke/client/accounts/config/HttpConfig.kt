package com.lykke.client.accounts.config

data class HttpConfig(val url: String,
                 val connectTimeout: Int,
                 val readTimeout: Int)
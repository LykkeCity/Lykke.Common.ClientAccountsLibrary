package com.lykke.client.accounts.http.impl

import com.lykke.client.accounts.config.HttpConfig
import com.lykke.client.accounts.http.ClientAccountsHttpClient
import com.lykke.client.accounts.http.HttpClient
import com.lykke.client.accounts.http.dto.ClientWalletsDto
import java.net.URL

class ClientAccountsHttpClientImpl(private val httpConfig: HttpConfig,
                                   private val httpClient: HttpClient) : ClientAccountsHttpClient {
    private companion object {
        val ALL_CLIENTS_WALLETS_PATH = "/api/client/wallets"
        val CONTINUATION_TOKEN_PARAM_NAME = "continuationToken"
    }

    private val clientsWalletsUrl: URL

    init {
        clientsWalletsUrl = URL(httpConfig.url).toURI().resolve(ALL_CLIENTS_WALLETS_PATH).toURL()
    }

    override fun getAllClientWallets(): List<ClientWalletsDto> {
        val result = ArrayList<ClientWalletsDto>()

        var continuationToken: String? = null
        do {
            val response = httpClient.get(
                clientsWalletsUrl,
                mapOf(CONTINUATION_TOKEN_PARAM_NAME to continuationToken),
                ClientWalletsDto::class.java,
                httpConfig.connectTimeout, httpConfig.readTimeout
            )
            continuationToken = response.continuationToken
            result.add(response)
        } while (continuationToken != null)

        return result
    }
}
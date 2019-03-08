package com.lykke.client.accounts

import com.lykke.client.accounts.config.Config
import com.lykke.client.accounts.http.impl.ClientAccountsHttpClientImpl
import com.lykke.client.accounts.http.impl.HttpCleintImpl

class ClientAccountCacheFactory {
    private companion object {
        val CLIENT_ACCOUNT_CACHE_BY_CONFIG = HashMap<Config, ClientAccountCache>()
    }

    @Synchronized
    fun get(config: Config): ClientAccountCache {
        return CLIENT_ACCOUNT_CACHE_BY_CONFIG.getOrPut(config) {
            val httpClient = ClientAccountsHttpClientImpl(config.clientAccountsUrl, HttpCleintImpl())
            val clientIdByWalletId = HashMap<String, String>()
            httpClient.getAllClientWallets().stream().flatMap { it.clients.stream() }
                .forEach { client -> client.wallets.forEach { walletId -> clientIdByWalletId[walletId] = client.clientId }}

            ClientAccountCache(clientIdByWalletId)
        }
    }
}
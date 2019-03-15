package com.lykke.client.accounts

import com.lykke.client.accounts.config.Config
import com.lykke.client.accounts.config.RabbitMqConfig
import com.lykke.client.accounts.http.impl.ClientAccountsHttpClientImpl
import com.lykke.client.accounts.http.impl.HttpCleintImpl
import com.lykke.client.accounts.rabbitmq.RabbitMqListenersFactory
import java.util.function.Consumer

class ClientAccountCacheFactory {
    private companion object {
        val CLIENT_ACCOUNT_CACHE_BY_CONFIG = HashMap<Config, ClientAccountsCacheImpl>()

        @Synchronized
        fun get(config: Config): ClientAccountsCache {
            val clientAccountCache = CLIENT_ACCOUNT_CACHE_BY_CONFIG.getOrPut(config) {
                val httpClient = ClientAccountsHttpClientImpl(config.clientAccountsUrl, HttpCleintImpl())
                val clientIdByWalletId = HashMap<String, String>()
                httpClient.getAllClientWallets().stream().flatMap { it.clients.stream() }
                    .forEach { client ->
                        client.wallets.forEach { walletId ->
                            clientIdByWalletId[walletId] = client.clientId
                        }
                    }

                ClientAccountsCacheImpl(clientIdByWalletId)
            }

            subscribeToClientAccountsUpdates(config.clientAccountsRabbitConfig, clientAccountCache)

            return clientAccountCache
        }

        fun shutdownAll() {
            RabbitMqListenersFactory
        }

        private fun subscribeToClientAccountsUpdates(rabbitMqConfig: RabbitMqConfig, clientAccountCache: ClientAccountsCacheImpl) {
            val rmqListener = RabbitMqListenersFactory.getListener(rabbitMqConfig)
            rmqListener.addListener(Consumer {
                clientAccountCache.add(it.clientId, it.walletId)
            })
        }
    }
}
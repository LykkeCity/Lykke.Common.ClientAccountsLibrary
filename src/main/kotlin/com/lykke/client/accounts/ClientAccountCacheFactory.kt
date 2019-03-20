package com.lykke.client.accounts

import com.lykke.client.accounts.config.Config
import com.lykke.client.accounts.config.RabbitMqConfig
import com.lykke.client.accounts.rabbitmq.RabbitMqListenersFactory
import java.util.function.Consumer

class ClientAccountCacheFactory {
    private companion object {
        val CLIENT_ACCOUNT_CACHE_BY_CONFIG = HashMap<Config, ClientAccountsCacheImpl>()

        @Synchronized
        fun get(config: Config): ClientAccountsCache {
            val clientAccountCache = CLIENT_ACCOUNT_CACHE_BY_CONFIG.getOrPut(config) {
                val clientIdByWalletId = HashMap<String, String>()
                ClientAccountsCacheImpl(clientIdByWalletId)
            }

            subscribeToClientAccountsUpdates(config.clientAccountsRabbitConfig, clientAccountCache)

            return clientAccountCache
        }

        fun shutdownAll() {
            RabbitMqListenersFactory.shutdownAll()
        }

        private fun subscribeToClientAccountsUpdates(
            rabbitMqConfig: RabbitMqConfig,
            clientAccountCache: ClientAccountsCacheImpl
        ) {
            val rmqListener = RabbitMqListenersFactory.getListener(rabbitMqConfig)
            rmqListener.addListener(Consumer {
                clientAccountCache.add(it.clientId, it.walletId)
            })
        }
    }
}
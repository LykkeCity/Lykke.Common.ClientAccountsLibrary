package com.lykke.client.accounts.rabbitmq

import com.lykke.client.accounts.config.RabbitMqConfig
import com.lykke.utils.logging.ThrottlingLogger
import java.lang.Exception

internal class RabbitMqListenersFactory {
    companion object {
        private val LOGGER = ThrottlingLogger.getLogger(RabbitMqListenersFactory::class.java.name)
        private val rabbitMqListenersToRabbitMqConfig = HashMap<RabbitMqConfig, ClientAccountsRmqListener>()

        @Synchronized
        fun getListener(config: RabbitMqConfig): ClientAccountsRmqListener {
            return rabbitMqListenersToRabbitMqConfig.getOrPut(config) {
                ClientAccountsRmqListener(config, ProtoDeserializer())
            }
        }

        fun shutdownAll() {
            rabbitMqListenersToRabbitMqConfig.values.forEach {
                try {
                    it.close()
                }
                catch (e: Exception) {
                    LOGGER.error("Error occurred on RMQ subscriber shutdown", e)
                }
            }
        }
    }
}
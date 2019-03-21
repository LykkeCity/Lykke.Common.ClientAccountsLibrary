package com.lykke.client.accounts.rabbitmq

import com.lykke.client.accounts.config.RabbitMqConfig
import com.lykke.client.accounts.incoming.ClientAccountMessages.WalletCreatedEvent as WalletCreatedEvent
import com.lykke.utils.logging.ThrottlingLogger
import com.lykke.utils.rabbit.Connector
import com.lykke.utils.rabbit.ConsumerFactory
import com.lykke.utils.rabbit.RabbitMqSubscriber
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingQueue
import java.util.function.Consumer
import kotlin.concurrent.thread
import com.lykke.utils.rabbit.RabbitMqConfig as UtilsRabbitMqConfig

class ClientAccountsRmqListener(
    private val rabbitMqConfig: RabbitMqConfig,
    private val messageDeserializer: Deserializer<WalletCreatedEvent>
) {

    private companion object {
        val LOGGER = ThrottlingLogger.getLogger(ClientAccountsRmqListener::class.java.simpleName)
    }

    private val listeners = CopyOnWriteArrayList<Consumer<WalletCreatedEvent>>()
    private val eventsQueue: BlockingQueue<ByteArray> = rabbitMqConfig.queue ?: LinkedBlockingQueue<ByteArray>()
    private val rabbitMqSubscriber: RabbitMqSubscriber
    private var started = false

    init {
        rabbitMqSubscriber = getRabbitMqSubscriber()
        start()
    }

    fun addListener(listener: Consumer<WalletCreatedEvent>) {
        listeners.add(listener)
    }

    @Synchronized
    fun start() {
        if (started) {
            throw IllegalStateException("Client accounts rmq listener already stared")
        }

        rabbitMqSubscriber.start()
        startEventProcessingLoop()

        started = true
    }

    fun close() {
        rabbitMqSubscriber.shutdown()
    }

    private fun startEventProcessingLoop() {
        thread(name = ClientAccountsRmqListener::class.java.simpleName) {
            while (true) {
                val event = eventsQueue.take()
                val message = messageDeserializer.deserialize(event)
                listeners.forEach {
                    try {
                        it.accept(message)
                    } catch (e: Exception) {
                        LOGGER.error("Encountered error during listener event processor", e)
                    }
                }
            }
        }
    }

    private fun getRabbitMqSubscriber(): RabbitMqSubscriber {

        return RabbitMqSubscriber(UtilsRabbitMqConfig(
            uri = rabbitMqConfig.uri,
            exchange = rabbitMqConfig.exchange,
            queue =  rabbitMqConfig.queueName,
            connectionTryInterval = null
        ),
            object : Connector {
                override fun createChannel(config: UtilsRabbitMqConfig): Channel {
                    val factory = ConnectionFactory()
                    config.host?.let { factory.host = it }
                    config.port?.let { factory.port = it }
                    config.uri?.let { factory.setUri(it) }
                    factory.setUri(config.uri)
                    factory.username = config.username
                    factory.password = config.password
                    factory.requestedHeartbeat = 30
                    factory.isAutomaticRecoveryEnabled = true

                    val connection = factory.newConnection()
                    val channel = connection!!.createChannel()

                    channel.exchangeDeclarePassive(config.exchange)
                    channel.queueDeclare(
                        config.queue,
                        rabbitMqConfig.durableQueue ?: false,
                        rabbitMqConfig.exclusiveQueue ?: false,
                        rabbitMqConfig.autoDeleteQueue ?: true,
                        null
                    )
                    channel.queueBind(config.queue, config.exchange, rabbitMqConfig.routingKey ?: "")
                    return channel
                }
            }, object : ConsumerFactory {
                override fun newConsumer(channel: Channel): com.rabbitmq.client.Consumer {
                    return EventConsumer(channel, eventsQueue)
                }
            })
    }
}
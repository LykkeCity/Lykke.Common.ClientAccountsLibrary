package com.lykke.client.accounts.config

import java.util.concurrent.BlockingQueue


data class RabbitMqConfig(
    val uri: String,
    val exchange: String,
    val queueName: String,
    val routingKey: String?,
    val durableQueue: Boolean?,
    val exclusiveQueue: Boolean?,
    val autoDeleteQueue: Boolean?,
    val queue: BlockingQueue<ByteArray>?
)
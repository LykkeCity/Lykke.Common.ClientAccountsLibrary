package com.lykke.client.accounts.config

import java.util.concurrent.BlockingQueue

data class RabbitMqConfig(
    val uri: String,
    val exchange: String,
    val queueName: String,
    val routingKey: String? = null,
    val durableQueue: Boolean? = null,
    val exclusiveQueue: Boolean? = null,
    val autoDeleteQueue: Boolean? = null,
    val queue: BlockingQueue<ByteArray>?
)
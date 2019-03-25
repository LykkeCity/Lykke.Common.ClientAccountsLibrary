package com.lykke.client.accounts.rabbitmq

interface Deserializer<T> {
    fun deserialize(message: ByteArray): T
}
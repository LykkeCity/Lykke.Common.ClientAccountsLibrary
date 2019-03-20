package com.lykke.client.accounts.config

data class Config(
    val redisConfig: RedisConfig,
    val clientAccountsRabbitConfig: RabbitMqConfig
)
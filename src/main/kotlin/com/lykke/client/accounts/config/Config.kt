package com.lykke.client.accounts.config

data class Config(
    val clientAccountsUrl: HttpConfig,
    val clientAccountsRabbitConfig: RabbitConfig
)
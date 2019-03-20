package com.lykke.client.accounts.redis

interface ClientAccountsAccessor {
    fun getAllClientsWallets(): List<ClientWalletsEntity>
}
package com.lykke.client.accounts.redis

import redis.clients.jedis.Jedis

class ClientAccountsRedisDbAccessor(val jedis: Jedis): ClientAccountsAccessor {
    private companion object {

    }

    override fun getAllClientsWallets(): List<ClientWalletsEntity> {
     }
}
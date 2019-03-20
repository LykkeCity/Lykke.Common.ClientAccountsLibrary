package com.lykke.client.accounts.redis

import com.lykke.client.accounts.config.RedisConfig
import redis.clients.jedis.Jedis

class JedisConnectionFactory {
    companion object {
        fun get(config: RedisConfig): Jedis {
            val jedis = Jedis(config.host, config.port, config.userSsl)
            jedis.select(config.dbIndex)
            return jedis
        }
    }
}
package com.lykke.client.accounts

import java.util.concurrent.ConcurrentHashMap

class ClientAccountsCacheImpl(private val clientIdByWalletId: ConcurrentHashMap<String, String>): ClientAccountsCache {
    internal fun delete(walletId: String) {
        clientIdByWalletId.remove(walletId)
    }

    internal fun add(clientId: String, walletId: String) {
        clientIdByWalletId[walletId] = clientId
    }

    override fun getClientByWalletId(walletId: String): String? {
        return clientIdByWalletId[walletId]
    }
}
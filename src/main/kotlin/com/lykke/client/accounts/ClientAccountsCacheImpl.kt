package com.lykke.client.accounts

import java.util.concurrent.ConcurrentHashMap

class ClientAccountsCacheImpl(private val clientIdByWalletId: ConcurrentHashMap<String, String>) : ClientAccountsCache {
    private val walletsByClientId = ConcurrentHashMap<String, MutableSet<String>>()

    init {
        clientIdByWalletId.forEach { walletId, clientId ->
            val wallets = walletsByClientId.putIfAbsent(clientId, ConcurrentHashMap.newKeySet())
            wallets!!.add(walletId)
        }
    }

    internal fun delete(walletId: String) {
        val clientId = clientIdByWalletId.remove(walletId) ?: return
        val wallets = walletsByClientId[clientId] ?: return
        wallets.remove(walletId)
    }

    internal fun add(clientId: String, walletId: String) {
        clientIdByWalletId[walletId] = clientId
        val wallets = walletsByClientId.putIfAbsent(clientId, ConcurrentHashMap.newKeySet())
        wallets!!.add(walletId)
    }

    override fun getClientByWalletId(walletId: String): String? {
        return clientIdByWalletId[walletId]
    }

    override fun getWalletsByClientId(clientId: String): Set<String> {
        return walletsByClientId[clientId] ?: emptySet()
    }
}
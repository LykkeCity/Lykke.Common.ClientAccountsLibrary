package com.lykke.client.accounts

import java.util.concurrent.ConcurrentHashMap

class ClientAccountsCacheImpl(private val clientIdByWalletId: ConcurrentHashMap<String, String>) : ClientAccountsCache {
    private val walletIdsByClientId = ConcurrentHashMap<String, MutableSet<String>>()

    init {
        clientIdByWalletId.forEach { walletId, clientId ->
            val walletIds = getWalletIds(clientId)
            walletIds.add(walletId)
        }
    }

    internal fun deleteWallet(walletId: String) {
        val clientId = clientIdByWalletId.remove(walletId) ?: return
        val wallets = walletIdsByClientId[clientId] ?: return
        wallets.remove(walletId)
    }

    internal fun addClientWallet(clientId: String, walletId: String) {
        clientIdByWalletId[walletId] = clientId
        val wallets = getWalletIds(clientId)
        wallets.add(walletId)
    }

    private fun getWalletIds(clientId: String): MutableSet<String> {
        return walletIdsByClientId.getOrPut(clientId) { ConcurrentHashMap.newKeySet() }
    }

    override fun getClientByWalletId(walletId: String): String? {
        return clientIdByWalletId[walletId]
    }

    override fun getWalletsByClientId(clientId: String): Set<String> {
        return walletIdsByClientId[clientId] ?: emptySet()
    }
}
package com.lykke.client.accounts

class ClientAccountsCacheImpl(initialData: Map<String, String>): ClientAccountsCache {
    private val clientIdByWalletId = HashMap<String, String>()

    init {
        clientIdByWalletId.putAll(initialData)
    }

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
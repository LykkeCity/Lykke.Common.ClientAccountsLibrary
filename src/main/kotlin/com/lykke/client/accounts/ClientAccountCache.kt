package com.lykke.client.accounts

class ClientAccountCache(initialData: Map<String, String>) {
    private val clientIdByWalletId = HashMap<String, String>()

    init {
        clientIdByWalletId.putAll(initialData)
    }

    fun delete(walletId: String) {
        clientIdByWalletId.remove(walletId)
    }

    fun add(clientId: String, walletId: String) {
        clientIdByWalletId[walletId] = clientId
    }

    fun getClientByWalletId(walletId: String): String? {
        return clientIdByWalletId[walletId]
    }
}
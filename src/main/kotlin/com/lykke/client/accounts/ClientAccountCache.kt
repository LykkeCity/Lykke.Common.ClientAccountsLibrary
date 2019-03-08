package com.lykke.client.accounts

class ClientAccountCache(initialData: Map<String, String>) {
    private val clientIdByWalletId = HashMap<String, String>()

    init {
        clientIdByWalletId.putAll(initialData)
    }

    fun getClientByWalletId(walletId: String): String? {
        return clientIdByWalletId[walletId]
    }
}
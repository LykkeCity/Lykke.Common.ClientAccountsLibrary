package com.lykke.client.accounts

interface ClientAccountsCache {
    fun getClientByWalletId(walletId: String): String?
    fun getWalletsByClientId(clientId: String): Set<String>
}
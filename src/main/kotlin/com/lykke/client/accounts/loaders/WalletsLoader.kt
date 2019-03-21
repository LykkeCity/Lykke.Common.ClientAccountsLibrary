package com.lykke.client.accounts.loaders

interface WalletsLoader {
    fun loadClientByWalletsMap(): Map<String, String>
}
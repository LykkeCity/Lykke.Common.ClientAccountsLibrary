package com.lykke.client.accounts.http

import com.lykke.client.accounts.http.dto.ClientWalletsDto

interface ClientAccountsHttpClient {
    fun getAllClientWallets(): List<ClientWalletsDto>
}
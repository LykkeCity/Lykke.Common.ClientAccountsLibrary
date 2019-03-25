package com.lykke.client.accounts.loaders.http

import com.lykke.client.accounts.ClientAccountsException
import com.lykke.client.accounts.http.generated.client.ApiClient
import com.lykke.client.accounts.http.generated.client.api.WalletsApi
import com.lykke.client.accounts.http.generated.client.model.AllWalletsResponseModel
import com.lykke.client.accounts.loaders.WalletsLoader
import com.lykke.utils.logging.ThrottlingLogger
import org.apache.commons.lang3.StringUtils
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap

class HttpWalletsLoaderImpl(url: String, connectionTimeout: Int) : WalletsLoader {
    private companion object {
        val LOGGER = ThrottlingLogger.getLogger(HttpWalletsLoaderImpl::class.java.simpleName)!!
    }

    private val client = WalletsApi(ApiClient()
        .setBasePath(url)
        .setConnectTimeout(connectionTimeout))

    override fun loadClientByWalletsMap(): ConcurrentHashMap<String, String> {
        val clientsByWallets = ConcurrentHashMap<String, String>()
        var continuationToken = StringUtils.EMPTY
        do {
            val clientWallets = getClientWallets(continuationToken)
            clientWallets.wallets.forEach { clientsByWallets[it.id] = it.clientId }

            continuationToken = clientWallets.continuationToken
            LOGGER.info("Loaded client wallets ${clientsByWallets.size}")
        } while (StringUtils.isNoneEmpty(continuationToken))

        return clientsByWallets
    }

    private fun getClientWallets(continuationToken: String): AllWalletsResponseModel {
        return try {
            client.getAll(continuationToken)
        } catch (e: Exception) {
            throw ClientAccountsException(
                "Exception occurred during client " +
                        "wallets information loading from client accounts http api", e
            )
        }
    }
}
package com.lykke.client.accounts

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ClientAccountsCacheTest {
    @Test
    fun testCacheInitialized() {
        val clientAccountsCache =
            ClientAccountsCacheImpl(mapOf("testWallet1" to "Client1", "testWallet2" to "Client2"))
        assertEquals("Client1", clientAccountsCache.getClientByWalletId("testWallet1"))
        assertEquals("Client2", clientAccountsCache.getClientByWalletId("testWallet2"))
    }

    @Test
    fun testAddNewEntryToCache() {
        val clientAccountsCache =
            ClientAccountsCacheImpl(mapOf("testWallet1" to "Client1", "testWallet2" to "Client2"))
        clientAccountsCache.add("Client3", "wallet3")
        assertEquals("Client1", clientAccountsCache.getClientByWalletId("testWallet1"))
        assertEquals("Client2", clientAccountsCache.getClientByWalletId("testWallet2"))
        assertEquals("Client3", clientAccountsCache.getClientByWalletId("testWallet3"))
    }

    @Test
    fun testDeleteEntryFromCache() {
        val clientAccountsCache =
            ClientAccountsCacheImpl(mapOf("testWallet1" to "Client1", "testWallet2" to "Client2"))
        clientAccountsCache.delete("testWallet1")
        assertNull(clientAccountsCache.getClientByWalletId("testWallet1"))
        assertEquals("Client2", clientAccountsCache.getClientByWalletId("testWallet2"))
    }
}
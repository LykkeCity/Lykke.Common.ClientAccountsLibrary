package com.lykke.client.accounts

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.concurrent.ConcurrentHashMap

class ClientAccountsCacheTest {
    @Test
    fun testCacheInitialized() {
        val clientAccountsCache =
            ClientAccountsCacheImpl(ConcurrentHashMap(mapOf("testWallet1" to "Client1", "testWallet2" to "Client2")))
        assertEquals("Client1", clientAccountsCache.getClientByWalletId("testWallet1"))
        assertEquals("Client2", clientAccountsCache.getClientByWalletId("testWallet2"))

        assertEquals(setOf("testWallet1"), clientAccountsCache.getWalletsByClientId("Client1"))
        assertEquals(setOf("testWallet2"), clientAccountsCache.getWalletsByClientId("Client2"))
    }

    @Test
    fun testAddNewEntryToCache() {
        val clientAccountsCache =
            ClientAccountsCacheImpl(ConcurrentHashMap(mapOf("testWallet1" to "Client1", "testWallet2" to "Client2")))
        clientAccountsCache.addClientWallet("Client3", "testWallet3")
        assertEquals("Client1", clientAccountsCache.getClientByWalletId("testWallet1"))
        assertEquals("Client2", clientAccountsCache.getClientByWalletId("testWallet2"))
        assertEquals("Client3", clientAccountsCache.getClientByWalletId("testWallet3"))

        assertEquals(setOf("testWallet3"), clientAccountsCache.getWalletsByClientId("Client3"))
    }

    @Test
    fun testDeleteEntryFromCache() {
        val clientAccountsCache =
            ClientAccountsCacheImpl(ConcurrentHashMap(mapOf("testWallet1" to "Client1", "testWallet2" to "Client2")))

        clientAccountsCache.deleteWallet("testWallet1")
        assertNull(clientAccountsCache.getClientByWalletId("testWallet1"))
        assertEquals("Client2", clientAccountsCache.getClientByWalletId("testWallet2"))

        assertEquals(emptySet<String>(), clientAccountsCache.getWalletsByClientId("Client1"))
        assertEquals(setOf("testWallet2"), clientAccountsCache.getWalletsByClientId("Client2"))
    }
}
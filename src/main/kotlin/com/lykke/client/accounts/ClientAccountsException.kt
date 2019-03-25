package com.lykke.client.accounts

class ClientAccountsException: Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: java.lang.Exception): super(message, cause)
}
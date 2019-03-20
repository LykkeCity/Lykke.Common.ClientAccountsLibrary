package com.lykke.client.accounts.rabbitmq

import com.lykke.client.accounts.incoming.ClientAccountMessages.WalletCreatedEvent as WalletCreatedEvent

class ProtoDeserializer: Deserializer<WalletCreatedEvent>  {
    override fun deserialize(message: ByteArray): WalletCreatedEvent {
        return WalletCreatedEvent.parseFrom(message)
    }
}
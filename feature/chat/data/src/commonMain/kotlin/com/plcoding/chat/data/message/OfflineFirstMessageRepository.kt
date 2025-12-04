package com.plcoding.chat.data.message

import com.plcoding.chat.data.database.safeDatabaseUpdate
import com.plcoding.chat.database.ChirpChatDatabase
import com.plcoding.chat.domain.message.MessageRepository
import com.plcoding.chat.domain.models.ChatMessageDeliveryStatus
import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.EmptyResult
import kotlin.time.Clock

class OfflineFirstMessageRepository(
    private val database: ChirpChatDatabase,
): MessageRepository {
    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        deliveryStatus: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return safeDatabaseUpdate {
            database.chatMessageDao.updateDeliveryStatusMessage(
                messageId = messageId,
                deliveryStatus = deliveryStatus.name,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        }
    }
}
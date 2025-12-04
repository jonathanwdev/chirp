package com.plcoding.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.plcoding.chat.database.entities.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Upsert
    suspend fun upsertMessage(message: ChatMessageEntity)

    @Upsert
    suspend fun upsertMessages(messages: List<ChatMessageEntity>)

    @Query("DELETE FROM chatmessageentity WHERE messageId = :messageId")
    suspend fun deleteMessageById(messageId: String)

    @Query("DELETE FROM chatmessageentity WHERE messageId in (:messageIds)")
    suspend fun deleteMessageById(messageIds: List<String>)

    @Query("SELECT * FROM chatmessageentity WHERE chatId = :chatId ORDER BY timestamp DESC")
    fun getMessagesByChatId(chatId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chatmessageentity WHERE messageId = :messageId")
    suspend fun getMessageById(messageId: String): ChatMessageEntity?

    @Query("""
            UPDATE chatmessageentity 
            SET deliveryStatus = :deliveryStatus, deliveryStatusTimestamp = :timestamp 
            WHERE messageId = :messageId
    """)
    suspend fun updateDeliveryStatusMessage(messageId: String, deliveryStatus: String, timestamp: Long)
}
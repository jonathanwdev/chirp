package com.plcoding.chat.data.chat

import com.plcoding.chat.data.dto.requests.CreateChatRequestDTO
import com.plcoding.chat.data.dto.responses.ChatDTO
import com.plcoding.chat.data.mappers.toDomain
import com.plcoding.chat.domain.chat.ChatService
import com.plcoding.chat.domain.models.Chat
import com.plcoding.core.data.networking.delete
import com.plcoding.core.data.networking.get
import com.plcoding.core.data.networking.post
import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.EmptyResult
import com.plcoding.core.domain.util.Result
import com.plcoding.core.domain.util.asEmptyResult
import com.plcoding.core.domain.util.map
import io.ktor.client.HttpClient

class KtorChatService(
    private val httpClient: HttpClient
): ChatService {
    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return httpClient
            .post<CreateChatRequestDTO, ChatDTO>(
                route = "/chat",
                body = CreateChatRequestDTO(otherUserIds = otherUserIds)

            ).map {
                it.toDomain()
            }
    }

    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return httpClient.get<List<ChatDTO>>(
            route = "/chat"
        ).map { chatDto ->
            chatDto.map { it.toDomain() }
        }
    }

    override suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote> {
        return httpClient.get<ChatDTO>(
            route = "/chat/$chatId"
        ).map { chatDto ->
            chatDto.toDomain()
        }
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return httpClient.delete<Unit>(
            route = "/chat/$chatId/leave",

        ).asEmptyResult()
    }

}
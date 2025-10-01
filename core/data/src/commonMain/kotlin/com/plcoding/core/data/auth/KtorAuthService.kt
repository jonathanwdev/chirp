package com.plcoding.core.data.auth

import com.plcoding.core.data.dto.requests.EmailRequestDTO
import com.plcoding.core.data.dto.requests.RegisterRequestDTO
import com.plcoding.core.data.networking.post
import com.plcoding.core.domain.auth.AuthService
import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class KtorAuthService(
    private val httpClient: HttpClient
) : AuthService {
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "auth/register",
            body = RegisterRequestDTO(
                username = username,
                email = email,
                password = password
            )
        )
    }

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "auth/resend-verification",
            body = EmailRequestDTO(
                email = email
            )
        )
    }
}
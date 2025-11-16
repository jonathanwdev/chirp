package com.plcoding.core.data.auth

import com.plcoding.core.data.dto.AuthInfoSerializable
import com.plcoding.core.data.dto.requests.EmailRequestDTO
import com.plcoding.core.data.dto.requests.LoginRequestDTO
import com.plcoding.core.data.dto.requests.RegisterRequestDTO
import com.plcoding.core.data.dto.requests.ResetPasswordRequestDTO
import com.plcoding.core.data.mappers.toDomain
import com.plcoding.core.data.networking.get
import com.plcoding.core.data.networking.post
import com.plcoding.core.domain.auth.AuthInfo
import com.plcoding.core.domain.auth.AuthService
import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.EmptyResult
import com.plcoding.core.domain.util.Result
import com.plcoding.core.domain.util.map
import io.ktor.client.HttpClient

class KtorAuthService(
    private val httpClient: HttpClient
) : AuthService {
    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote> {
        return httpClient.post<LoginRequestDTO, AuthInfoSerializable>(
            route = "auth/login",
            body = LoginRequestDTO(
                email = email,
                password = password
            )
        ).map { authInfoSerializable ->
            authInfoSerializable.toDomain()
        }
    }

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

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        return httpClient.get(
            route = "/auth/verify",
            queryParams = mapOf("token" to token)
        )
    }

    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "auth/forgot-password",
            body = EmailRequestDTO(
                email = email
            )
        )

    }

    override suspend fun resetPassword(
        password: String,
        token: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "auth/forgot-password",
            body = ResetPasswordRequestDTO(
                password = password,
                token = token
            )
        )
    }
}
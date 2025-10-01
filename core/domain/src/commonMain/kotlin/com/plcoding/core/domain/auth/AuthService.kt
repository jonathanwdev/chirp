package com.plcoding.core.domain.auth

import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.EmptyResult

interface  AuthService {
    suspend fun register(
        username: String,
        email: String,
        password: String
    ): EmptyResult<DataError.Remote>

    suspend fun resendVerificationEmail(
        email: String
    ): EmptyResult<DataError.Remote>
}
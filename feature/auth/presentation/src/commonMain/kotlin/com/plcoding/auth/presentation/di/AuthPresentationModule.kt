package com.plcoding.auth.presentation.di

import com.plcoding.auth.presentation.screens.email_verification.EmailVerificationViewModel
import com.plcoding.auth.presentation.screens.forgot_password.ForgotPasswordViewModel
import com.plcoding.auth.presentation.screens.login.LoginViewModel
import com.plcoding.auth.presentation.screens.register.RegisterViewModel
import com.plcoding.auth.presentation.screens.register_success.RegisterSuccessViewModel
import com.plcoding.auth.presentation.screens.reset_password.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::ResetPasswordViewModel)
}
package com.plcoding.chat.presentation.di

import com.plcoding.chat.presentation.screens.chat_detail.ChatDetailViewModel
import com.plcoding.chat.presentation.screens.chat_list.ChatListViewModel
import com.plcoding.chat.presentation.screens.chat_list_detail.ChatListDetailViewModel
import com.plcoding.chat.presentation.screens.create_chat.CreateChatViewModel
import com.plcoding.chat.presentation.screens.manage_chat.ManageChatViewModel
import com.plcoding.chat.presentation.screens.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chatPresentationModule = module {
    viewModelOf(::ChatListDetailViewModel)
    viewModelOf(::CreateChatViewModel)
    viewModelOf(::ChatListViewModel)
    viewModelOf(::ChatDetailViewModel)
    viewModelOf(::ManageChatViewModel)
    viewModelOf(::ProfileViewModel)
}
package com.plcoding.chirp

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.plcoding.chirp.navigation.ExternalUriHandler

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var shouldShowSplashScreen = true


        installSplashScreen().setKeepOnScreenCondition {
            shouldShowSplashScreen
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        handleChatMessageDeepLinking(intent)
        setContent {
            App(
                onAuthenticationChecked = {
                    shouldShowSplashScreen = false
                }

            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleChatMessageDeepLinking(intent)
    }

    private fun handleChatMessageDeepLinking(intent: Intent) {
        val chatId = intent.getStringExtra("chatId") ?: intent.extras?.getString("chatId")
        if(chatId != null) {
            val deepLinkUrl = "chirp://chat_detail/$chatId"
            ExternalUriHandler.onNewUri(deepLinkUrl)
        }
    }
}

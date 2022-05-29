package com.ericg.jichat

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ericg.jichat.screens.ChatScreen
import com.ericg.jichat.ui.theme.JiChatTheme
import com.ericg.jichat.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION") // 🤪
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            val chatsViewModel: ChatViewModel = hiltViewModel()
            JiChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState,
                        topBar = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "JiChat",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                IconButton(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    onClick = {
                                        chatsViewModel.deleteChats()
                                    }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = "delete icon"
                                    )
                                }
                            }
                        }
                    ) {
                        ChatScreen()
                    }
                }
            }
        }
    }

    companion object {
        fun getTime(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("h:mm a")
            return current.format(formatter)
        }
    }
}

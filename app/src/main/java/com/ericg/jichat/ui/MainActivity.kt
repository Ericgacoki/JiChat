package com.ericg.jichat.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ericg.jichat.components.ChatInputLayout
import com.ericg.jichat.components.ChatItem
import com.ericg.jichat.model.Chat
import com.ericg.jichat.model.SenderType
import com.ericg.jichat.ui.theme.JiChatTheme
import com.ericg.jichat.viewmodel.ChatViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION") // 🤪
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            val systemUiController = rememberSystemUiController()
            val chatsViewModel: ChatViewModel = hiltViewModel()
            val chats =
                chatsViewModel.uiState.collectAsState(initial = ChatState())
                    .value.chats.collectAsState(initial = emptyList())

            val lazyListState = rememberLazyListState()

            LaunchedEffect(key1 = chats.value.size) {
                lazyListState.animateScrollToItem(chats.value.size)
            }

            JiChatTheme {
                val statusBarColor = MaterialTheme.colorScheme.surfaceVariant
                val useDarkIcons = isSystemInDarkTheme()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = statusBarColor,
                        darkIcons = useDarkIcons.not()
                    )
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            SmallTopAppBar(
                                title = {
                                    Text(
                                        text = "JiChat",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                },
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ), actions = {
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
                            )
                        },
                        bottomBar = {
                            ChatInputLayout { message ->
                                val humanChat = Chat(
                                    sender = SenderType.HUMAN,
                                    message = message,
                                    time = getTime()
                                )
                                chatsViewModel.saveChat(humanChat)
                                chatsViewModel.getBotResponse(message)
                            }
                        }
                    ) {
                        LazyColumn(state = lazyListState, modifier = Modifier.padding(it)) {
                            items(chats.value) { chat ->
                                ChatItem(chat = chat)
                            }
                            item {
                                AnimatedVisibility(visible = chatsViewModel.uiState.value.loading) {
                                    Box(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MaterialTheme.colorScheme.primary)
                                    ) {
                                        Text(
                                            text = "Typing...",
                                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                            }
                            item {
                                val error = chatsViewModel.uiState.value.error
                                AnimatedVisibility(error.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.error)
                                            .fillMaxWidth(),
                                    ) {
                                        Text(
                                            text = error,
                                            modifier = Modifier.padding(all = 4.dp),
                                            color = MaterialTheme.colorScheme.onError
                                        )
                                    }
                                }
                            }
                        }
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

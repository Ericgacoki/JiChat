package com.ericg.jichat.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ericg.jichat.MainActivity.Companion.getTime
import com.ericg.jichat.model.Chat
import com.ericg.jichat.model.SenderType
import com.ericg.jichat.ui.theme.JiChatTheme
import com.ericg.jichat.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val chats = chatViewModel.chats.collectAsState(initial = emptyList())

    val chatsState = rememberLazyListState()
    LaunchedEffect(key1 = chats.value.size) {
        chatsState.animateScrollToItem(chats.value.size)
    }

    LazyColumn(
        state = chatsState,
        modifier = Modifier.fillMaxHeight(0.85F),
    ) {
        items(chats.value) { chat ->
            ChatItem(chat = chat)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        ChatInputLayout { message ->
            val humanChat = Chat(
                sender = SenderType.HUMAN,
                message = message,
                time = getTime()
            )
            chatViewModel.saveChat(humanChat)
            chatViewModel.getBotResponse(message)
        }
    }
}

@Composable
fun ChatItem(chat: Chat) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = CenterVertically),
        horizontalAlignment = if (chat.sender == SenderType.BOT) Alignment.Start else Alignment.End
    ) {
        Text(
            text = if (chat.sender == SenderType.BOT) "Bot" else "You",
            fontSize = 10.sp,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
        )

        Surface(
            elevation = 2.dp,
            shape = if (chat.message.length < 24) CircleShape else RoundedCornerShape(4.dp),
            modifier = Modifier.widthIn(max = 220.dp)
        ) {
            Text(
                text = chat.message,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
        }

        Text(
            text = chat.time,
            fontSize = 8.sp,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 6.dp),
        )
    }
}


@Composable
fun ChatInputLayout(
    onSendClick: (message: String) -> Unit
) {
    var message by remember { mutableStateOf("") }
    // FIXME: Use colors from Material-You
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color.LightGray,
        unfocusedBorderColor = Color.LightGray
    )
    Row(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = message,
            maxLines = 8,
            onValueChange = { newValue ->
                if (newValue.length <= 128) {
                    message = newValue
                }
            },
            colors = textFieldColors,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(0.8F)
                .widthIn(min = 150.dp),
            placeholder = {
                Text(text = "Type something")
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            )
        )

        FloatingActionButton(modifier = Modifier
            .padding(12.dp)
            .size(42.dp),
            onClick = {
                if (message.trim().isNotEmpty()) {
                    onSendClick(message)
                    message = ""
                }
            }) {
            Icon(
                imageVector = Icons.Rounded.Send,
                contentDescription = "send icon"
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun ChatItemPre() {
    val chat = Chat(
        sender = SenderType.BOT,
        message = "Hello, I'm Eric",
        time = "02:55"
    )
    JiChatTheme() {
        Column() {
            ChatItem(chat = chat.copy(sender = SenderType.HUMAN))
            ChatItem(chat = chat.copy(message = "Hello, Eric! How can I help you? Would you like a joke?"))
            ChatInputLayout {

            }
        }
    }
}
package com.ericg.jichat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.ericg.jichat.model.Chat
import com.ericg.jichat.model.SenderType

@OptIn(ExperimentalMaterial3Api::class)
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
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
        )

        Card(
            shape = RoundedCornerShape(
                percent = if (checkLineCount(
                        text = chat.message,
                        maxWidth = 300
                    ) < 2
                ) 80 else 8
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (chat.sender == SenderType.HUMAN)
                    MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.tertiaryContainer,
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .widthIn(max = 240.dp)
        ) {
            Text(
                text = chat.message,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
        }

        Text(
            text = chat.time,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 6.dp),
        )
    }
}

@Composable
fun ChatInputLayout(
    onSendClick: (message: String) -> Unit
) {
    var message by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        value = message,
        maxLines = 8,
        onValueChange = { newValue ->
            if (newValue.length <= 128) {
                message = newValue
            }
        },
        trailingIcon = {
            AnimatedVisibility(visible = message.trim().isNotEmpty()) {
                IconButton(onClick = {
                    onSendClick(message)
                    message = ""
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Send,
                        contentDescription = "send icon"
                    )
                }
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(
            if (checkLineCount(
                    text = message,
                    maxWidth = 400
                ) < 3
            ) 100 else 8
        ),
        placeholder = {
            Text(text = "Type something")
        },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )
}

@Composable
private fun checkLineCount(text: String, maxWidth: Int): Int {
    return Paragraph(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        constraints = Constraints(maxWidth = maxWidth),
        density = LocalDensity.current,
        fontFamilyResolver = LocalFontFamilyResolver.current,
    ).lineCount
}
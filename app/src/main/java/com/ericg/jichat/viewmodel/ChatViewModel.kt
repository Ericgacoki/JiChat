package com.ericg.jichat.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericg.jichat.data.repository.ChatRepository
import com.ericg.jichat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repo: ChatRepository) : ViewModel() {
    private var _botResponse = mutableStateOf<String>("")
    val botResponse: MutableState<String> = _botResponse

    fun getBotResponse(message: String) {
        viewModelScope.launch {
            when (val response = repo.getBotResponse(message)) {
                is Resource.Success -> {
                    _botResponse.value = response.data!!.botResponse
                }
                else -> {
                    Timber.e("Error loading bot response")
                }
            }
        }
    }
}

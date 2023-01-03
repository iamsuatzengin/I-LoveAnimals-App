package com.suatzengin.i_love_animals.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suatzengin.i_love_animals.domain.use_case.RegisterUseCase
import com.suatzengin.i_love_animals.presentation.auth.AuthState
import com.suatzengin.i_love_animals.util.Resource
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState>
        get() = _state

    private val _eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val eventFlow = _eventChannel.receiveAsFlow()

    fun createUserWithEmail(email: String?, password: String?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            registerUseCase.invoke(email = email, password = password)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            _eventChannel.send(UiEvent.NavigateToLogin)
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(isLoading = false, message = result.message ?: "Register failed!")
                            }
                        }
                    }
                }
        }
    }
}

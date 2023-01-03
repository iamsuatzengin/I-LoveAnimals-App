package com.suatzengin.i_love_animals.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.suatzengin.i_love_animals.domain.use_case.LoginUseCase
import com.suatzengin.i_love_animals.presentation.auth.AuthState
import com.suatzengin.i_love_animals.util.Resource
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState>
        get() = _state

    private val _eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val eventFlow = _eventChannel.receiveAsFlow()

    val currentUser: FirebaseUser?
        get() = loginUseCase.getCurrentUser()

    init {
        viewModelScope.launch {
            if(currentUser != null){
                _eventChannel.send(UiEvent.NavigateToHome)
            }
        }
    }

    fun loginWithEmail(email: String?, password: String?){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            loginUseCase.invoke(email = email, password = password).collectLatest { result ->
                when(result){
                    is Resource.Success -> {

                        _state.update {
                            it.copy(isLoading = false, message = result.message ?: "Login")
                        }
                        _eventChannel.send(UiEvent.NavigateToHome)
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, message = result.message ?: "Login failed!") }
                    }
                }
            }
        }
    }
}

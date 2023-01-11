package com.suatzengin.i_love_animals.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.suatzengin.i_love_animals.domain.use_case.UseCases
import com.suatzengin.i_love_animals.util.Resource
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _stateLogin = MutableStateFlow(AuthState())
    val stateLogin: StateFlow<AuthState>
        get() = _stateLogin

    private val _stateRegister = MutableStateFlow(AuthState())
    val stateRegister: StateFlow<AuthState>
        get() = _stateRegister

    private val _eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val eventFlow = _eventChannel.receiveAsFlow()

    val currentUser: FirebaseUser?
        get() = useCases.getUserUseCase.invoke()

    init {
        viewModelScope.launch {
            if (currentUser != null) {
                _eventChannel.send(UiEvent.NavigateToHome)
            }
        }
    }

    fun loginWithEmail(email: String?, password: String?) {
        viewModelScope.launch {
            _stateLogin.update { it.copy(isLoading = true) }
            useCases.loginUseCase.invoke(email = email, password = password)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            _stateLogin.update {
                                it.copy(isLoading = false, message = result.message ?: "Login")
                            }
                            _eventChannel.send(UiEvent.NavigateToHome)
                        }
                        is Resource.Error -> {
                            _stateLogin.update {
                                it.copy(
                                    isLoading = false,
                                    message = result.message ?: "Login failed!"
                                )
                            }
                        }
                    }
                }
        }
    }

    fun createUserWithEmail(email: String?, password: String?, fullName: String?) {
        viewModelScope.launch {
            _stateRegister.update { it.copy(isLoading = true) }
            useCases.registerUseCase.invoke(email = email, password = password, fullName = fullName)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            _eventChannel.send(UiEvent.NavigateToLogin)
                        }
                        is Resource.Error -> {
                            _stateRegister.update {
                                it.copy(isLoading = false)
                            }
                            _eventChannel.send(
                                UiEvent.ShowMessage(
                                    result.message ?: "Register Failed!"
                                )
                            )
                        }
                    }
                }
        }
    }
}
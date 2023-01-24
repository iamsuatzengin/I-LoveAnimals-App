package com.suatzengin.i_love_animals.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.suatzengin.i_love_animals.domain.model.User
import com.suatzengin.i_love_animals.domain.use_case.UseCases
import com.suatzengin.i_love_animals.util.Resource
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val currentUser: FirebaseUser?
        get() = useCases.getUserUseCase.invoke()

    private val _eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val eventFlow = _eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState>
        get() = _state

    init {
        getUserProfile()
    }
    private fun getUserProfile(){
        useCases.profileUseCase.invoke(currentUser?.email!!).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.update { it.copy(user = result.data) }
                }
                is Resource.Error -> {
                    _eventChannel.send(UiEvent.ShowMessage(result.message ?: "Error"))
                }
            }
        }.launchIn(viewModelScope)
    }
    fun logOut() {
        viewModelScope.launch {
            try {
                useCases.signOutUseCase.invoke()
                _eventChannel.send(UiEvent.LogOut)
            } catch (e: Exception) {
                _eventChannel.send(UiEvent.ShowMessage(e.localizedMessage ?: "An error!"))
            }
        }
    }
}

data class ProfileUiState(
    val user: User? = User(),
    val message: String = ""
)
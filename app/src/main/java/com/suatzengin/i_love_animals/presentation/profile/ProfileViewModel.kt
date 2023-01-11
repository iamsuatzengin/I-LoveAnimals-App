package com.suatzengin.i_love_animals.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.suatzengin.i_love_animals.domain.use_case.UseCases
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    val currentUser: FirebaseUser?
        get() = useCases.getUserUseCase.invoke()

    private val _eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val eventFlow = _eventChannel.receiveAsFlow()

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
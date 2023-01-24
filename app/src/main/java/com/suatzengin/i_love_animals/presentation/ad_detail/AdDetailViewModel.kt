package com.suatzengin.i_love_animals.presentation.ad_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.suatzengin.i_love_animals.domain.use_case.UseCases
import com.suatzengin.i_love_animals.util.Resource
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdDetailViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _channel = Channel<UiEvent>(Channel.BUFFERED)
    val eventFlow = _channel.receiveAsFlow()

    private val _stateStatus = MutableStateFlow(false)
    val stateStatus: StateFlow<Boolean>
        get() = _stateStatus
    private val currentUser: FirebaseUser?
        get() = useCases.getUserUseCase.invoke()

    fun updateAdStatus(id: String, status: Boolean) {
        viewModelScope.launch {
            when (val result = useCases.changeAdStatus(id = id, status = status)) {
                is Resource.Success -> {
                    useCases.updateUserAdCompletedCount(currentUser?.email!!)
                    _channel.send(UiEvent.ShowMessage(result.data ?: "Error!"))
                }

                is Resource.Error -> {
                    _channel.send(UiEvent.ShowMessage(result.message ?: "Error!"))
                }
            }
        }
    }

    fun setStatus(status: Boolean) {
        _stateStatus.value = status
    }
}

package com.suatzengin.i_love_animals.presentation.ad_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suatzengin.i_love_animals.domain.use_case.ad.ChangeAdStatus
import com.suatzengin.i_love_animals.util.Resource
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdDetailViewModel @Inject constructor(
    private val changeAdStatus: ChangeAdStatus
): ViewModel() {

    private val _channel = Channel<UiEvent>(Channel.BUFFERED)
    val eventFlow = _channel.receiveAsFlow()


    fun changeStatus(id: String, status: Boolean){
        viewModelScope.launch {
            when(val result = changeAdStatus(id = id, status = status)){
                is Resource.Success -> {
                    _channel.send(UiEvent.ShowMessage(result.data ?: ""))
                }
                is Resource.Error -> {
                    _channel.send(UiEvent.ShowMessage(result.message ?: "Error!"))
                }
            }
        }
    }
}
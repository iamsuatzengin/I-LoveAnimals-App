package com.suatzengin.i_love_animals.presentation.post_ad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suatzengin.i_love_animals.domain.model.Advertisement
import com.suatzengin.i_love_animals.domain.model.MyLocation
import com.suatzengin.i_love_animals.domain.use_case.UseCases
import com.suatzengin.i_love_animals.util.Resource
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PostAdViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {


    private val _location = MutableLiveData<MyLocation>()
    val location: LiveData<MyLocation>
        get() = _location

    private val _channel = Channel<UiEvent>(Channel.BUFFERED)
    val eventFlow = _channel.receiveAsFlow()

    fun addLocation(location: MyLocation) {
        _location.value = location
    }

    fun postNewAd(advertisement: Advertisement) {
        viewModelScope.launch {
            useCases.postAdUseCase.invoke(advertisement = advertisement)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            val successMessage = result.data ?: ""
                            useCases.updateUserAdCount(email = advertisement.authorEmail!!)
                            _channel.send(UiEvent.ShowMessage(successMessage))
                        }
                        is Resource.Error -> {
                            _channel.send(UiEvent.ShowMessage(result.message ?: ""))
                        }
                    }
                }
        }
    }
}

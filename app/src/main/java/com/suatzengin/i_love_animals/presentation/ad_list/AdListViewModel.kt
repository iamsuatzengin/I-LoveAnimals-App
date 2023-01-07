package com.suatzengin.i_love_animals.presentation.ad_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import com.suatzengin.i_love_animals.domain.use_case.UseCases
import com.suatzengin.i_love_animals.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class AdListViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AdListState())
    val state: StateFlow<AdListState>
        get() = _state

    fun getAllAd(status: Boolean) {
        useCases.getAllAdUseCase.invoke(direction = Query.Direction.DESCENDING, status = status)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(list = result.data ?: emptyList(), isLoading = false)
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(message = result.message ?: "Something went wrong!")
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(list = emptyList(), isLoading = true)
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }


}

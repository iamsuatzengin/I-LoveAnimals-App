package com.suatzengin.i_love_animals.presentation.ad_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query.Direction
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

    init {
        getAllAd()
    }

    private fun getAllAd() {
        useCases.getAllAdUseCase.invoke(
            direction = _state.value.direction,
            status = _state.value.status
        )
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

    fun setStatus(status: Boolean, selectedTabPosition: Int) {
        _state.update { it.copy(status = status, selectedTabPosition = selectedTabPosition) }
        getAllAd()
    }

    fun setDirection(direction: Direction) {
        _state.update { it.copy(direction = direction) }
        getAllAd()
    }

}

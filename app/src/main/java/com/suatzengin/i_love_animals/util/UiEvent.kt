package com.suatzengin.i_love_animals.util

sealed class UiEvent {
    object NavigateToLogin: UiEvent()
    object NavigateToHome: UiEvent()
    class ShowMessage(val message: String): UiEvent()
    object LogOut: UiEvent()
}
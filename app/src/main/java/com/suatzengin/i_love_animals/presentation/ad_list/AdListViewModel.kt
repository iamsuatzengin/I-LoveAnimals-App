package com.suatzengin.i_love_animals.presentation.ad_list

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AdListViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {


    fun signOut(){
        auth.signOut()
    }
}
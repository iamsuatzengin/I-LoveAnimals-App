package com.suatzengin.i_love_animals.presentation.post_ad

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PostAdViewModel @Inject constructor(

) : ViewModel() {

    val address =  MutableLiveData<String>()

    fun addAddress(adrs: String){
        address.value = adrs
    }

}
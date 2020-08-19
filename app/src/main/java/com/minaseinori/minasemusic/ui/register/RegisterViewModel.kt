package com.minaseinori.minasemusic.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.minaseinori.minasemusic.logic.Repository
import com.minaseinori.minasemusic.logic.model.RegisterRequest

class RegisterViewModel : ViewModel() {
    private val registerLiveData = MutableLiveData<RegisterRequest>()

    val tokenLiveData = Transformations.switchMap(registerLiveData) {
        Repository.register(it)
    }

    fun register(request: RegisterRequest) {
        registerLiveData.value = request
    }
}
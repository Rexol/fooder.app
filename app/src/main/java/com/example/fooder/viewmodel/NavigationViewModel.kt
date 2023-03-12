package com.example.fooder.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NavigationViewModel: ViewModel() {
    var selected by mutableStateOf(0)

    fun updateSelected(newValue: Int) {
        selected = newValue
    }
}
package com.waracle.thecakelist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waracle.thecakelist.usecase.GetAllCakesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CakeListViewModel @Inject constructor(
    getAllCakesUseCase: GetAllCakesUseCase,
) : ViewModel() {
    val getAllCakes = getAllCakesUseCase()

    private val _displayedCakeDetails: MutableStateFlow<String?> by lazy { MutableStateFlow(null) }
    val displayedCakeDetails: StateFlow<String?> = _displayedCakeDetails

    // pass null to dismiss cake details
    fun showCakeDetails(cakeDetails: String?) {
        viewModelScope.launch {
            _displayedCakeDetails.emit(cakeDetails)
        }
    }
}
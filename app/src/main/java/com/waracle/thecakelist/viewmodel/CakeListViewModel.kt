package com.waracle.thecakelist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waracle.thecakelist.model.Cake
import com.waracle.thecakelist.usecase.GetAllCakesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CakeListViewModel @Inject constructor(
    val getAllCakesUseCase: GetAllCakesUseCase,
) : ViewModel() {

    private val _allCakes: MutableStateFlow<List<Cake>> by lazy { MutableStateFlow(emptyList()) }
    val allCakes: StateFlow<List<Cake>> = _allCakes

    private val _displayedCakeDetails: MutableStateFlow<String?> by lazy { MutableStateFlow(null) }
    val displayedCakeDetails: StateFlow<String?> = _displayedCakeDetails

    private val _isLoading: MutableStateFlow<Boolean> by lazy { MutableStateFlow(false) }
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        refresh()
    }

    // pass null to dismiss cake details
    fun showCakeDetails(cakeDetails: String?) {
        viewModelScope.launch {
            _displayedCakeDetails.emit(cakeDetails)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.emit(true)
            val retrievedCakes = getAllCakesUseCase.invoke().first()
            _allCakes.emit(retrievedCakes)
            _isLoading.emit(false)
        }
    }
}
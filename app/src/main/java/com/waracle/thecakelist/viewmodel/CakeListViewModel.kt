package com.waracle.thecakelist.viewmodel

import androidx.lifecycle.ViewModel
import com.waracle.thecakelist.usecase.GetAllCakesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CakeListViewModel @Inject constructor(
    getAllCakesUseCase: GetAllCakesUseCase,
) : ViewModel() {
    val getAllCakes = getAllCakesUseCase()
}
package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.di.ServiceLocator

object GenderAgeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GenderAgeViewModel(
            useCase = ServiceLocator.genderAgeUseCase,
            mapper = ServiceLocator.voMapper
        ) as T
    }
}
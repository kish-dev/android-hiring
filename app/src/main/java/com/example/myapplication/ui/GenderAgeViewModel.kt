package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.GenderAgeUseCase
import com.example.myapplication.ui.mapper.GenderAgeModelVOMapper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class GenderAgeViewModel(
    private val useCase: GenderAgeUseCase,
    private val mapper: GenderAgeModelVOMapper,
) : ViewModel() {
    private val _uiState = MutableLiveData<UiState>(UiState.Idle)
    val uiState: LiveData<UiState> = _uiState

    private val handler = CoroutineExceptionHandler() { _, exception ->
        _uiState.value = UiState.Error(exception.message ?: "Blank error")
    }

    fun getAllowState() {
        viewModelScope.launch(handler) {

            val genderAgeModelResponse = when (val state = _uiState.value) {
                null, is UiState.Error, UiState.Idle -> {
                    null
                }

                is UiState.Data -> {
                    val gender = state.gender
                    val pickedAge = state.pickedAge
                    updateState { current ->
                        current.copy(isLoading = true)
                    }
                    useCase.isAllowed(
                        genderAgeModelRequest =
                        mapper.mapDomainToData(
                            gender = gender,
                            pickedAge = pickedAge
                        )
                    )
                }
            }




            genderAgeModelResponse?.onFailure { exception ->
                _uiState.value = UiState.Error(exception.message ?: "Blank error")
            }?.onSuccess { genderAgeModelResponse ->
                updateState { current ->
                    if (genderAgeModelResponse.isAllowed) {
                        current.copy(allowed = ALLOWED_STATE.ALLOWED, isLoading = false)
                    } else {
                        current.copy(allowed = ALLOWED_STATE.DISALLOWED, isLoading = false)
                    }

                }
            }
        }
    }

    fun pickAge(value: Int) {
        updateState { current ->
            current.copy(pickedAge = value)
        }
    }

    fun pickGender(newGender: Gender) {
        updateState { current ->
            current.copy(gender = newGender)
        }
    }

    private fun updateState(update: (UiState.Data) -> UiState.Data) {
        when (val current = _uiState.value) {
            is UiState.Data -> _uiState.value = update(current)
            else -> _uiState.value = update(UiState.Data())
        }
    }
}

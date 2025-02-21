package com.example.myapplication.ui

enum class Gender {
    MALE,
    FEMALE,
    UNKNOWN
}

enum class ALLOWED_STATE {
    ALLOWED,
    DISALLOWED,
    UNKNOWN,
}

sealed class UiState {
    data class Data(
        val pickedAge: Int = 0,
        val gender: Gender = Gender.UNKNOWN,
        val allowed: ALLOWED_STATE = ALLOWED_STATE.UNKNOWN,
        val isLoading: Boolean = false,
    ) : UiState()

    data class Error(val exception: String) : UiState()
    data object Idle : UiState()
}
package com.example.myapplication.domain

interface GenderAgeUseCase {
    suspend fun isAllowed(genderAgeModelRequest: GenderAgeModelRequest):
            Result<GenderAgeModelResponse>
}
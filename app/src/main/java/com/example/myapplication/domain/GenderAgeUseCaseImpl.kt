package com.example.myapplication.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenderAgeUseCaseImpl(private val repository: SocketRepository): GenderAgeUseCase {
    override suspend fun isAllowed(genderAgeModelRequest: GenderAgeModelRequest):
            Result<GenderAgeModelResponse> =
        withContext(Dispatchers.IO) {
            repository.sendRequest(genderAgeModelRequest)
        }
}
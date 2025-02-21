package com.example.myapplication.domain

interface SocketRepository {
    suspend fun sendRequest(request: GenderAgeModelRequest): Result<GenderAgeModelResponse>
}
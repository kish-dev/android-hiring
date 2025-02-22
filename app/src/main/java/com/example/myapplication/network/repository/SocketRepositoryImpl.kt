package com.example.myapplication.network.repository

import com.example.myapplication.domain.GenderAgeModelRequest
import com.example.myapplication.domain.GenderAgeModelResponse
import com.example.myapplication.domain.SocketRepository
import com.example.myapplication.network.SocketConnectionException
import com.example.myapplication.network.SocketManager
import com.example.myapplication.network.TestRequest
import com.example.myapplication.network.TestResponse
import com.example.myapplication.network.mapper.GenderAgeModelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException

class SocketRepositoryImpl(
    private val socketManager: SocketManager,
    private val mapper: GenderAgeModelMapper
): SocketRepository {
    private val connectionMutex = Mutex()

    override suspend fun sendRequest(request: GenderAgeModelRequest):
            Result<GenderAgeModelResponse> {
        return try {
            ensureConnected()
            val response = withContext(Dispatchers.IO) {
                socketManager.send(mapper.mapDomainToData(request))
                socketManager.receive()
            }
            Result.success(response).map { result -> mapper.mapDataToDomain(result) }
        } catch (e: Exception) {
            Result.failure(handleException(e))
        }
    }

    private suspend fun ensureConnected() {
        if (!isConnected()) {
            connectionMutex.withLock {
                if (!isConnected()) {
                    reconnect()
                }
            }
        }
    }

    private fun isConnected(): Boolean {
        return socketManager.isConnected()
    }

    private suspend fun reconnect() {
        withContext(Dispatchers.IO) {
            try {
                socketManager.close()
                socketManager.connect()
            } catch (e: Exception) {
                throw SocketConnectionException("Reconnect failed", e)
            }
        }
    }

    private fun handleException(e: Exception): Exception {
        return when (e) {
            is SocketConnectionException -> e
            is IOException -> SocketConnectionException("Network error", e)
            else -> SocketConnectionException("Unexpected error", e)
        }
    }
}
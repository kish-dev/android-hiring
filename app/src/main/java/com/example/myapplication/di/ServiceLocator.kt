package com.example.myapplication.di

import com.example.myapplication.domain.GenderAgeUseCase
import com.example.myapplication.domain.GenderAgeUseCaseImpl
import com.example.myapplication.domain.SocketRepository
import com.example.myapplication.network.SocketManager
import com.example.myapplication.network.SocketManagerImpl
import com.example.myapplication.network.mapper.GenderAgeModelMapper
import com.example.myapplication.network.mapper.RequestMapper
import com.example.myapplication.network.mapper.ResponseMapper
import com.example.myapplication.network.repository.SocketRepositoryImpl
import com.example.myapplication.ui.mapper.GenderAgeModelVOMapper
import com.google.gson.Gson

object ServiceLocator {
    private const val SERVER_ADDRESS = "challenge.ciliz.com"
    private const val SERVER_PORT = 2222

    private val gson = Gson()

    private val requestMapper = RequestMapper(gson)
    private val responseMapper = ResponseMapper(gson)

    private val socketManager: SocketManager = SocketManagerImpl(
        address = SERVER_ADDRESS,
        port = SERVER_PORT,
        requestMapper = requestMapper,
        responseMapper = responseMapper
    )

    private val dataMapper = GenderAgeModelMapper()
    val voMapper = GenderAgeModelVOMapper()

    private val socketRepository: SocketRepository =
        SocketRepositoryImpl(socketManager = socketManager, mapper = dataMapper)

    val genderAgeUseCase: GenderAgeUseCase =
        GenderAgeUseCaseImpl(repository = socketRepository)
}
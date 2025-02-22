package com.example.myapplication.network

interface SocketManager {
    fun connect()
    fun send(request: TestRequest, reSendTimes: Int = 3)
    fun close()
    fun isConnected(): Boolean
    fun receive(): TestResponse
}
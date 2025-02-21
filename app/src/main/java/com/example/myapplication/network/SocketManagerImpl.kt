package com.example.myapplication.network

import android.util.Log
import com.example.myapplication.network.mapper.RequestMapper
import com.example.myapplication.network.mapper.ResponseMapper
import java.io.DataInputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer

private const val TAG = "SocketManager"

class SocketManagerImpl(
    private val address: String,
    private val port: Int,
    private val requestMapper: RequestMapper,
    private val responseMapper: ResponseMapper,
): SocketManager {
    private var socket: Socket? = null
    private val lock = Any()

    @Synchronized
    override fun connect() {
        synchronized(lock) {
            close()
            socket = Socket(address, port)
        }
    }

    @Synchronized
    override fun send(request: TestRequest, reSendTimes: Int) {
        synchronized(lock) {
            checkConnection()
            val message = requestMapper.toJson(request) // <- request

            Log.i(TAG, "sending: $message")

            val messageBytes = message.toByteArray()
            val lengthBytes = ByteBuffer.allocate(4).putInt(messageBytes.size).array()

            try {
                val outputStream = socket?.getOutputStream()
                outputStream?.write(lengthBytes)
                outputStream?.write(messageBytes)
                outputStream?.flush()
            } catch (e: Exception) {
                if(reSendTimes < 0) {
                    throw e
                } else {
                    connect()
                    send(request, reSendTimes - 1)
                    Log.e(TAG, e.message ?: "Send network error")
                }

            }

        }
    }

    @Synchronized
    override fun receive(): TestResponse {
        synchronized(lock) {
            checkConnection()
            val inputStream = DataInputStream(socket?.getInputStream())

            val lengthBytes = ByteArray(4)
            inputStream.readFully(lengthBytes)
            val length = ByteBuffer.wrap(lengthBytes).int

            val buffer = ByteArray(length)
            inputStream.readFully(buffer)
            val message = String(buffer, 0, length)

            Log.d(TAG, "received: $message")

            return responseMapper.fromJson(message)
        }
    }

    @Synchronized
    override fun close() {
        synchronized(lock) {
            try {
                socket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Error closing socket", e)
            } finally {
                socket = null
            }
        }
    }

    override fun isConnected(): Boolean {
        return synchronized(lock) {
            socket?.isConnected ?: false
        }
    }

    private fun checkConnection() {
        if (!isConnected()) {
            throw SocketConnectionException("Not connected to socket")
        }
    }
}

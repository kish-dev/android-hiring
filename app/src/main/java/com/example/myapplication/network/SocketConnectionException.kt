package com.example.myapplication.network

import java.io.IOException

class SocketConnectionException(
    message: String? = null,
    cause: Throwable? = null
) : IOException(message, cause)
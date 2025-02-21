package com.example.myapplication.network.mapper

import com.example.myapplication.network.TestRequest
import com.google.gson.Gson

class RequestMapper(
    private val gson: Gson
) {

    fun toJson(request: TestRequest): String {
        return gson.toJson(request)
    }
}
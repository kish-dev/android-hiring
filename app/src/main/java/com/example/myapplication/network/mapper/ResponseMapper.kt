package com.example.myapplication.network.mapper

import android.util.Log
import com.example.myapplication.network.TestResponse
import com.google.gson.Gson

class ResponseMapper(
    private val gson: Gson
) {
    fun fromJson(json: String): TestResponse {
        return try {
            gson.fromJson(json, TestResponse::class.java)
        } catch (e: Exception) {
            Log.e("ResponseMapper", "Error parsing JSON: ${e.message}")
            throw e
        }
    }

}
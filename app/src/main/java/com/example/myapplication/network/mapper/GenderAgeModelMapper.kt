package com.example.myapplication.network.mapper

import com.example.myapplication.domain.GenderAgeModelRequest
import com.example.myapplication.domain.GenderAgeModelResponse
import com.example.myapplication.network.TestRequest
import com.example.myapplication.network.TestResponse

class GenderAgeModelMapper {
    fun mapDomainToData(genderAgeModelRequest: GenderAgeModelRequest): TestRequest {
        return TestRequest(
            gender = genderAgeModelRequest.gender,
            age = genderAgeModelRequest.pickedAge
        )
    }

    fun mapDataToDomain(testResponse: TestResponse): GenderAgeModelResponse {
        return GenderAgeModelResponse(isAllowed = testResponse.allowed)
    }
}
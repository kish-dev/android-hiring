package com.example.myapplication.ui.mapper

import com.example.myapplication.domain.GenderAgeModelRequest
import com.example.myapplication.ui.Gender

class GenderAgeModelVOMapper {
    fun mapDomainToData(gender: Gender, pickedAge: Int): GenderAgeModelRequest {
        return GenderAgeModelRequest(
            gender = when(gender) {
                Gender.MALE -> "m"
                Gender.FEMALE -> "f"
                Gender.UNKNOWN -> "u"
            },
            pickedAge = pickedAge
        )
    }
}
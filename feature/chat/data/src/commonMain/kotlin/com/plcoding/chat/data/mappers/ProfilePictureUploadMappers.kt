package com.plcoding.chat.data.mappers

import com.plcoding.chat.data.dto.responses.ProfilePictureUploadResponseDTO
import com.plcoding.chat.domain.models.ProfilePictureUploadUrls

fun ProfilePictureUploadResponseDTO.toDomain(): ProfilePictureUploadUrls {
    return ProfilePictureUploadUrls(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers
    )
}
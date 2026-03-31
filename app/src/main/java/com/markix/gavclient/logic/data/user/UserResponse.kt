package com.markix.gavclient.logic.data.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse (
    val id: Int,
    val firstname: String,
    val lastname: String,
    val status: Int,
    val mail: String,
    val group: String
)
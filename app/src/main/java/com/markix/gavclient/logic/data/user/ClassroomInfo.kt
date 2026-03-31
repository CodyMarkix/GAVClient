package com.markix.gavclient.logic.data.user

import kotlinx.serialization.Serializable

@Serializable
data class ClassroomInfo (
    val id: Int? = null,
    val name: String? = null,
    val mail: String? = null,
    val classroom: String? = null,
    val enabled: Int? = 1,
    val modified: String? = null
)
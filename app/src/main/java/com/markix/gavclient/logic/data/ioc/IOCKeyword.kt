package com.markix.gavclient.logic.data.ioc

import kotlinx.serialization.Serializable

@Serializable
data class IOCKeyword(
    var id: Int? = null,
    var type: Int? = null,
    var keyword: String? = null
)
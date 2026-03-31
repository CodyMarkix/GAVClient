package com.markix.gavclient.logic.data.ioc

import kotlinx.serialization.Serializable

@Serializable
data class IOCTopicData (
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
)
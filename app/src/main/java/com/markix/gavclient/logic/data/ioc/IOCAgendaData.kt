package com.markix.gavclient.logic.data.ioc

import kotlinx.serialization.Serializable

@Serializable
data class IOCAgendaData (
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val status: Int? = null,
    val archived: Int? = null,
    val roles: List<Int>? = null
)
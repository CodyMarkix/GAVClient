package com.markix.gavclient.logic.data

import kotlinx.serialization.Serializable

@Serializable
data class SeminarSlot(
    var id: Int? = null,
    var title: String? = null,
    var position: Int? = null,
    var visible: Int? = null
)
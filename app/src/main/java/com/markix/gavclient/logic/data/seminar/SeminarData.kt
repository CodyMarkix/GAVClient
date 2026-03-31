package com.markix.gavclient.logic.data.seminar

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class SeminarRegistryData @OptIn(ExperimentalTime::class) constructor(
    var since: Instant? = null,
    var priority: Int? = null
)

@Serializable
data class SeminarData(
    var id: Int? = null,
    var slot: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var lectors: String? = null,
    var status: Int? = null,
    var capacity: Int? = null,
    var registry: SeminarRegistryData? = null
)
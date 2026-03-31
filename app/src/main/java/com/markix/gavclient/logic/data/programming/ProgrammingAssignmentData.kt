package com.markix.gavclient.logic.data.programming

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class ProgrammingAssignmentData @OptIn(ExperimentalTime::class) constructor(
    var id: Int? = null,
    var title: String? = null,
    var minimum: Int? = null,
    var maximum: Int? = null,
    var bonus: Int? = null,
    var result: Int? = null,
    var validfrom: Instant? = null,
    var validto: Instant? = null,
    var completed: Instant? = null,
    var unlocked: Int? = null,
    var cancelled: Int? = null,
    var modified: Instant? = null,
)
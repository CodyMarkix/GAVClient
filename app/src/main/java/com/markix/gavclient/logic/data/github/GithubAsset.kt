package com.markix.gavclient.logic.data.github

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class GithubAsset @OptIn(ExperimentalTime::class) constructor(
    var url: String? = null,
    var id: Int? = null,
    var nodeId: String? = null,
    var name: String? = null,
    var label: String? = null,
    var uploader: GithubUser? = null,
    var contentType: String? = null,
    var state: String? = null,
    var size: Int? = null,
    var digest: String? = null,
    var downloadCount: Int? = null,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
    var browserDownloadUrl: String? = null
)
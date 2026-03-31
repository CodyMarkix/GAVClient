package com.markix.gavclient.logic.data.github

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class GithubAsset @OptIn(ExperimentalTime::class) constructor(
    var url: String? = null,
    var id: Int? = null,
    var node_id: String? = null,
    var name: String? = null,
    var label: String? = null,
    var uploader: GithubUser? = null,
    var content_type: String? = null,
    var state: String? = null,
    var size: Int? = null,
    var digest: String? = null,
    var download_count: Int? = null,
    var created_at: Instant? = null,
    var updated_at: Instant? = null,
    var browser_download_url: String? = null
)
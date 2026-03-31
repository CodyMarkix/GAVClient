package com.markix.gavclient.logic.data.github

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

// Breaking naming conventions is necessary here and the rest of
// this package because it's either that or you break JSON serialization
@Serializable
data class GithubRelease @OptIn(ExperimentalTime::class) constructor(
    var url: String? = null,
    var assets_url: String? = null,
    var upload_url: String? = null,
    var html_url: String? = null,
    var id: Int? = null,
    var author: GithubUser? = null,
    var node_id: String? = null,
    var tag_name: String? = null,
    var target_commitish: String? = null,
    var name: String? = null,
    var draft: Boolean = false,
    var immutable: Boolean = false,
    var prerelease: Boolean = false,
    var created_at: Instant? = null,
    var updated_at: Instant? = null,
    var published_at: Instant? = null,
    var assets: List<GithubAsset>? = null,
    var tarball_url: String? = null,
    var zipball_url: String? = null,
    var body: String? = null
)
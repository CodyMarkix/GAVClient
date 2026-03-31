package com.markix.gavclient.logic.data.github

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class GithubRelease @OptIn(ExperimentalTime::class) constructor(
    var url: String? = null,
    var assetsUrl: String? = null,
    var uploadUrl: String? = null,
    var htmlUrl: String? = null,
    var id: Int? = null,
    var author: GithubUser? = null,
    var nodeId: String? = null,
    var tagName: String? = null,
    var targetCommitish: String? = null,
    var name: String? = null,
    var draft: Boolean = false,
    var immutable: Boolean = false,
    var prerelease: Boolean = false,
    val createdAt: Instant? = null,
    var publishedAt: Instant? = null,
    var assets: List<GithubAsset>? = null,
    var tarballUrl: String? = null,
    var zipballUrl: String? = null,
    var body: String? = null
)
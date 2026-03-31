package com.markix.gavclient.logic.data.github

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class GithubUser (
    var login: String? = null,
    var id: Int? = null,
    var node_id: String? = null,
    var avatar_url: String? = null,
    var gravatar_id: String? = null,
    var url: String? = null,
    var html_url: String? = null,
    var followers_url: String? = null,
    var following_url: String? = null,
    var gists_url: String? = null,
    var starred_url: String? = null,
    var subscriptions_url: String? = null,
    var organizations_url: String? = null,
    var repos_url: String? = null,
    var events_url: String? = null,
    var received_events_url: String? = null,
    var type: String? = null,
    var user_view_type: String? = null,
    var site_admin: Boolean? = null
)
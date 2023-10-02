package com.example.loltube.model

import com.google.gson.annotations.SerializedName

data class YoutubeChannelInfo(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("items")
    val items: List<ChannelItem>?
)

data class ChannelItem(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("snippet")
    val snippet: ChannelSnippet
)

data class ChannelSnippet(
    @SerializedName("thumbnails")
    val thumbnails: ThumbNail
)
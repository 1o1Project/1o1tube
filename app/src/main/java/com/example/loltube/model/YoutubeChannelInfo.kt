package com.example.loltube.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class YoutubeChannelInfo(
    @SerializedName("kind")
    @Expose
    val kind: String,

    @SerializedName("etag")
    @Expose
    val etag: String,

    @SerializedName("items")
    @Expose
    val items: List<ChannelItem>?
)

data class ChannelItem(
    @SerializedName("kind")
    @Expose
    val kind: String,

    @SerializedName("etag")
    @Expose
    val etag: String,

    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("snippet")
    @Expose
    val snippet: ChannelSnippet
)

data class ChannelSnippet(
    @SerializedName("thumbnails")
    @Expose
    val thumbnails: ThumbNail
)
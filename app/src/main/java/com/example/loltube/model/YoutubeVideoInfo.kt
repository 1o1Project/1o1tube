package com.example.loltube.model

import com.google.gson.annotations.SerializedName


data class YoutubeVideoInfo(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("items")
    val items: List<TrendItem>?,
    @SerializedName("nextPageToken")
    val nextPageToken: String?
)

data class TrendItem(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("snippet")
    val snippet: Snippet,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("contentDetails")
    val contentDetails: ContentDetails,
    @SerializedName("statistics")
    val statistics: Statistics
)

data class ContentDetails(
    @SerializedName("duration")
    val duration: String
)

data class Statistics(
    @SerializedName("viewCount")
    val viewCount: String? = ""
)
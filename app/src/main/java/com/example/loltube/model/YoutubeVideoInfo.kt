package com.example.loltube.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class YoutubeVideoInfo(
    @SerializedName("kind")
    @Expose
    val kind: String,
    @SerializedName("etag")
    @Expose
    val etag: String,
    @SerializedName("items")
    @Expose
    val items: List<TrendItem>?,
    @SerializedName("nextPageToken")
    @Expose
    val nextPageToken: String?
)

data class TrendItem(
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
    val snippet: Snippet,

    @SerializedName("tags")
    @Expose
    val tags: List<String>,

    @SerializedName("contentDetails")
    @Expose
    val contentDetails: ContentDetails,

    @SerializedName("statistics")
    @Expose
    val statistics: Statistics
)

data class ContentDetails(
    @SerializedName("duration")
    @Expose
    val duration: String
)

data class Statistics(
    @SerializedName("viewCount")
    @Expose
    val viewCount: String? = ""
)
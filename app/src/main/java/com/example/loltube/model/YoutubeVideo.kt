package com.example.loltube.model

import com.google.gson.annotations.SerializedName

data class YoutubeVideo(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("nextPageToken")
    val nextPageToken: String,
    @SerializedName("regionCode")
    val regionCode: String,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo,
    @SerializedName("items")
    val items: List<Items>?,
)

data class PageInfo(
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("resultsPerPage")
    val resultsPerPage: Int
)

data class Items(
    @SerializedName("id")
    val id: Id,
    @SerializedName("snippet")
    val snippet: Snippet
)

data class Id(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("videoId")
    val videoId: String
)

data class Snippet(
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("channelId")
    val channelId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thumbnails")
    val thumbnails: ThumbNail,
    @SerializedName("publishTime")
    val publishTime: String,
    @SerializedName("channelTitle")
    val channelTitle: String,
)

data class ThumbNail(
    @SerializedName("medium")
    val medium: Medium
)

data class Medium(
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
)
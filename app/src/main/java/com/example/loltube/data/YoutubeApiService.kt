package com.example.loltube.data

import com.example.loltube.model.YoutubeChannelInfo
import com.example.loltube.model.YoutubeVideo
import com.example.loltube.model.YoutubeVideoInfo
import com.example.loltube.util.Constants.Companion.AUTH_HEADER
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {

    @GET("search")
    suspend fun getYouTubeVideos(
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("q") query: String,
        @Query("order") videoOrder: String,
        @Query("type") videoType: String = "video",
        @Query("maxResults") maxResults: Int,
        @Query("channelId") channelId: String = "",
        @Query("part") part: String = "snippet",
    ): Response<YoutubeVideo>

    @GET("search")
    suspend fun getYouTubeMoreVideos(
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("q") query: String,
        @Query("pageToken") nextPageToken: String,
        @Query("order") videoOrder: String,
        @Query("type") videoType: String = "video",
        @Query("maxResults") maxResults: Int,
        @Query("channelId") channelId: String = "",
        @Query("part") part: String = "snippet",
    ): Response<YoutubeVideo>

    @GET("videos")
    suspend fun getVideoInfo(
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("id") videoId: String,
        @Query("part") part: String = "contentDetails, statistics",
    ): Response<YoutubeVideoInfo>

    @GET("videos")
    suspend fun getYoutubeTrendVideos(
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("regionCode") regionCode: String,
        @Query("maxResults") maxResults: Int,
        @Query("chart") chart: String = "mostPopular",
        @Query("part") part: String = "snippet, contentDetails, statistics",
    ): Response<YoutubeVideoInfo>

    @GET("videos")
    suspend fun getMostPopular(
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("part") part: String = "snippet",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") regionCode: String = "KR",
        @Query("videoCategoryId") videoCategoryId: String,
        @Query("maxResults") maxResults: Int
    ): Response<YoutubeVideoInfo>

    @GET("videos")
    suspend fun getNextMostPopular(
        @Query("part") part: String = "snippet",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") regionCode: String = "KR",
        @Query("videoCategoryId") videoCategoryId: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("pageToken") pageToken: String
    ): Response<YoutubeVideoInfo>

    @GET("videos")
    suspend fun getCategory(
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("part") part: String = "snippet",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") regionCode: String = "KR",
        @Query("videoCategoryId") videoCategoryId: String,
        @Query("maxResults") maxResults: Int
    ): Response<YoutubeVideoInfo>

    @GET("videos")
    suspend fun getNextCategory(
        @Query("part") part: String = "snippet",
        @Query("chart") chart: String = "mostPopular",
        @Query("regionCode") regionCode: String = "KR",
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("videoCategoryId") videoCategoryId: String,
        @Query("maxResults") maxResults: Int,
        @Query("pageToken") pageToken: String
    ): Response<YoutubeVideoInfo>

    @GET("search")
    suspend fun getChannel(
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") videoType: String,
        @Query("maxResults") maxResults: Int,
        @Query("reginCode") regionCode: String
    ): Response<YoutubeVideo>

    @GET("search")
    suspend fun getNextChannel(
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") videoType: String,
        @Query("maxResults") maxResults: Int,
        @Query("reginCode") regionCode: String,
        @Query("pageToken") pageToken: String
    ): Response<YoutubeVideo>

    @GET("channels")
    suspend fun getYoutubeChannelInfo(
        @Query("key") apiKey: String = AUTH_HEADER,
        @Query("id") channelId: String,
        @Query("part") part: String = "snippet",
    ): Response<YoutubeChannelInfo>
}
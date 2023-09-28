package com.example.loltube.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.nio.channels.Channel

sealed interface ApiState<out T> {
    class Success<T>(val data: T, val nextPageToken: String) : ApiState<T>
    class Error<T>(val data: T?, val message: String) : ApiState<T>
    object Loading : ApiState<Nothing>
}

class ShortsRepositoryImpl : ShortsRepository {
    private val api = RetrofitInstance.api

    override suspend fun getShorts() = flow {
        emit(ApiState.Loading)
        try {
            val response =
                api.getYouTubeVideos(query = "LCK 쇼츠", videoOrder = "viewCount", maxResults = 10)

            if (response.isSuccessful) {
                val item = response.body()?.items?.filter {
                    it.snippet.description.contains("short") || it.snippet.description.contains("Short")
                            || it.snippet.description.contains("쇼츠") || it.snippet.title.contains("short")
                            || it.snippet.title.contains("Short") || it.snippet.title.contains("쇼츠")
                }.orEmpty()
                Log.d("getYoutube", item.toString())
                emit(ApiState.Success(item, response.body()!!.nextPageToken))
            } else {
                emit(ApiState.Error(null, response.errorBody().toString()))
            }

        } catch (e: Exception) {
            emit(ApiState.Error(null, "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getNextShorts(nextPageToken: String) = flow {
        emit(ApiState.Loading)

        try {
            val response =
                api.getYouTubeMoreVideos(query = "LCK 쇼츠", videoOrder = "viewCount", maxResults = 10, nextPageToken = nextPageToken)

            if (response.isSuccessful) {
                val item = response.body()?.items?.filter {
                    it.snippet.description.contains("short") || it.snippet.description.contains("Short")
                            || it.snippet.description.contains("쇼츠") || it.snippet.title.contains("short")
                            || it.snippet.title.contains("Short") || it.snippet.title.contains("쇼츠")
                }.orEmpty()
                emit(ApiState.Success(item, response.body()!!.nextPageToken))
            }
        } catch (e: Exception) {
            emit(ApiState.Error(null, "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)
}
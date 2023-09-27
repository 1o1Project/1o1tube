package com.example.loltube.data

import com.example.loltube.model.Items
import kotlinx.coroutines.flow.Flow

interface ShortsRepository {
    suspend fun getShorts() : Flow<ApiState<List<Items>>>

    suspend fun getNextShorts(nextPageToken: String) : Flow<ApiState<List<Items>>>
}
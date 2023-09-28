package com.example.loltube.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loltube.data.ApiState
import com.example.loltube.data.ShortsRepository
import com.example.loltube.model.Items
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    class Success<T> (val data : T) : UiState<T>
    class Error (val message : String) : UiState<Nothing>
}

class ShortsViewModel(private val shortsRepository: ShortsRepository) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<Items>>>(UiState.Loading)
    val uiState : LiveData<UiState<List<Items>>>
        get() = _uiState

    private var curList: MutableList<Items> = mutableListOf()
    private var nextPageToken: String = ""

    init {
        getShorts()
    }

    private fun getShorts() {
        viewModelScope.launch {
            shortsRepository
                .getShorts()
                .collectLatest {
                    when(it) {
                        is ApiState.Loading -> _uiState.value = UiState.Loading
                        is ApiState.Error -> _uiState.value = UiState.Error(it.message)
                        is ApiState.Success -> {
                            _uiState.value = UiState.Success(it.data)
                            curList = it.data.toMutableList()
                            nextPageToken = it.nextPageToken
                        }
                    }
                }
        }
    }

    fun getNextShorts() {
        viewModelScope.launch {
            shortsRepository
                .getNextShorts(nextPageToken)
                .collectLatest {
                    when(it) {
                        is ApiState.Loading -> _uiState.value = UiState.Loading
                        is ApiState.Error -> _uiState.value = UiState.Error(it.message)
                        is ApiState.Success -> {
                            _uiState.value = UiState.Success(curList.apply { addAll(it.data) })
                            nextPageToken = it.nextPageToken
                        }
                    }
                }
        }
    }

}
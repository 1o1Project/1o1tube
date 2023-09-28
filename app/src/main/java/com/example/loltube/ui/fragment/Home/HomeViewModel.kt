package com.example.loltube.ui.fragment.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.data.YoutubeApiService
import com.example.loltube.model.LOLModel
import com.example.loltube.model.SearchItemModel
import java.util.concurrent.atomic.AtomicLong

class HomeViewModel(
    private val idGenerate: AtomicLong,
    private val apiServiceInstance: YoutubeApiService
) : ViewModel() {


    private val _listForPopular: MutableLiveData<List<LOLModel>> = MutableLiveData()
    val listForPopular: LiveData<List<LOLModel>> get() = _listForPopular

    private val _listForCategory: MutableLiveData<List<LOLModel>> = MutableLiveData()
    val listForCategory: LiveData<List<LOLModel>> get() = _listForCategory

    private val _listForChannel: MutableLiveData<List<LOLModel>> = MutableLiveData()
    val listForChannel: LiveData<List<LOLModel>> get() = _listForChannel


    // 인피니티 스크롤 관련 변수

    private val _searchResults = MutableLiveData<List<SearchItemModel>>()
    val searchResults: LiveData<List<SearchItemModel>> get() = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    var isSearchFinished = false

    fun addPopularItem(
        item: LOLModel?
    ) {
        if (item == null) {
            return
        }
        val currentList = listForPopular.value.orEmpty().toMutableList()
        currentList.add(
            item.copy(
                id = idGenerate.getAndIncrement()
            )
        )
        _listForPopular.value = currentList
    }

    fun addCategoryItem(
        item: LOLModel?
    ) {
        if (item == null) {
            return
        }
        val currentList = listForCategory.value.orEmpty().toMutableList()
        currentList.add(
            item.copy(
                id = idGenerate.getAndIncrement()
            )
        )
        _listForCategory.value = currentList
    }

    fun addChannelItem(item: LOLModel) {
        val currentList = listForChannel.value.orEmpty().toMutableList()
        currentList.add(
            item.copy(
                id = idGenerate.getAndIncrement()
            )
        )
        _listForChannel.value = currentList
    }

    fun clearCategory() {
        val currentList = listForCategory.value.orEmpty().toMutableList()
        currentList.clear()
        _listForCategory.value = currentList
    }
    fun clearChannel() {
        val currentList = listForChannel.value.orEmpty().toMutableList()
        currentList.clear()
        _listForChannel.value = currentList
    }

}

class HomeViewModelFactory : ViewModelProvider.Factory {

    private val idGenerate = AtomicLong(1L)
    private val apiServiceInstance = RetrofitInstance.api

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(idGenerate, apiServiceInstance) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}


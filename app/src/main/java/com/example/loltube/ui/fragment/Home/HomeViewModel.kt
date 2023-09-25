package com.example.loltube.ui.fragment.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.atomic.AtomicLong

class HomeViewModel(
    private val idGenerate: AtomicLong
) : ViewModel() {

    private val _list: MutableLiveData<List<HomeModel>> = MutableLiveData()
    val list : LiveData<List<HomeModel>> get() = _list

    fun addHomeItems(
        items: List<HomeModel>?
    ) {
        if(items == null) {
            return
        }

        val currentList = list.value.orEmpty().toMutableList()
        currentList.addAll(items)
        _list.value = currentList
    }
    fun addHomeItem(
        item: HomeModel?
    ) {
        if(item == null) {
            return
        }

        val currentList = list.value.orEmpty().toMutableList()
        currentList.add(item)
        _list.value = currentList
    }

}
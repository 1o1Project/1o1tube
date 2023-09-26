package com.example.loltube.ui.fragment.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loltube.model.LOLModel
import java.util.concurrent.atomic.AtomicLong

class HomeViewModel(
    private val idGenerate: AtomicLong
) : ViewModel() {

    private val _list: MutableLiveData<List<LOLModel>> = MutableLiveData()
    val list : LiveData<List<LOLModel>> get() = _list

    private val _listForCategory: MutableLiveData<List<LOLModel>> = MutableLiveData()
    val listForCategory : LiveData<List<LOLModel>> get() = _listForCategory

//    fun addHomeItems(
//        items: List<HomeModel>?
//    ) {
//        if(items == null) {
//            return
//        }
//
////        val currentList = list.value.orEmpty().toMutableList()
////        currentList.addAll(items)
//        _list.value = items(copy(
//
//        ))
//    }

    fun addHomeItem(
        item: LOLModel?
    ) {
        if(item == null) {
            return
        }
            val currentList = list.value.orEmpty().toMutableList()
            currentList.add(item.copy(
                id = idGenerate.getAndIncrement()
            ))
            _list.value = currentList
    }

    fun addCategoryItem(
        item: LOLModel?
    ) {
        if(item == null) {
            return
        }
            val currentList = listForCategory.value.orEmpty().toMutableList()
            currentList.add(item.copy(
                id = idGenerate.getAndIncrement()
            ))
            _listForCategory.value = currentList
    }

    fun cleareCategoryItem() {
        val currentList = listForCategory.value.orEmpty().toMutableList()
        currentList.clear()
        _listForCategory.value = currentList
    }
}

class HomeViewModelFactory : ViewModelProvider.Factory {

    private val idGenerate = AtomicLong(1L)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(idGenerate) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}


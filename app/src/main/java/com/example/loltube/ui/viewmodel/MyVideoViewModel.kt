package com.example.loltube.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.loltube.data.SharedPrefInstance
import com.example.loltube.model.LOLModel

class MyVideoViewModel : ViewModel() {
    val bookmarkList : LiveData<List<LOLModel>> = SharedPrefInstance.getInstance().getBookmarkList().asLiveData()
}
package com.example.loltube.ui.fragment.Home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeModel(
    val id: Long? = -1,
    val thumnail: String?,
    val title: String?
) : Parcelable

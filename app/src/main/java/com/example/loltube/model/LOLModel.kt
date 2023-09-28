package com.example.loltube.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LOLModel (
    val id: Long? = -1,
    val thumbnail: String?,
    val title: String?,
    val description: String?
) : Parcelable

package com.example.loltube

import android.app.Application

class MyApplication : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: Application
    }
}
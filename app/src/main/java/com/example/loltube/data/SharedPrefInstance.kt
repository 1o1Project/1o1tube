package com.example.loltube.data

import android.content.Context
import android.content.SharedPreferences
import com.example.loltube.MyApplication
import com.example.loltube.model.LOLModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SharedPrefInstance private constructor() {
    private val application = MyApplication.instance
    private val prefs: SharedPreferences by lazy { application.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE) }
    private val editor: SharedPreferences.Editor by lazy { prefs.edit() }

    suspend fun setBookmarkListPref(values: List<LOLModel>) {
        withContext(Dispatchers.IO) {
            val gson = Gson()
            val json = gson.toJson(values)
            editor.putString(PREF_KEY, json)
            editor.apply()
        }
    }

    suspend fun addBookmarkPref(value: LOLModel) {
        withContext(Dispatchers.IO) {
            val json = prefs.getString(PREF_KEY, null)
            val gson = Gson()

            val storedData : MutableList<LOLModel> = gson.fromJson(json, object : TypeToken<MutableList<LOLModel>?>() {}.type) ?: mutableListOf()

            if (storedData.none { it.title == value.title && it.thumbnail == value.thumbnail }) {
                storedData.add(value)

                editor.putString(PREF_KEY, gson.toJson(storedData))
                editor.apply()
            }
        }
    }

    suspend fun isBookmark(value: LOLModel) =
        withContext(Dispatchers.IO) {
            val json = prefs.getString(PREF_KEY, null)
            val gson = Gson()

            val storedData : MutableList<LOLModel> = gson.fromJson(json, object : TypeToken<MutableList<LOLModel>?>() {}.type) ?: mutableListOf()

            if (storedData.none { it.title == value.title && it.thumbnail == value.thumbnail }) {
                return@withContext false
            }
            return@withContext true
        }

    suspend fun deleteBookmarkPref(value: LOLModel) {
        withContext(Dispatchers.IO) {
            val json = prefs.getString(PREF_KEY, null)
            val gson = Gson()

            val storedData : MutableList<LOLModel> = gson.fromJson(json, object : TypeToken<MutableList<LOLModel>?>() {}.type) ?: mutableListOf()
            storedData.remove(storedData.find { it.title == value.title && it.thumbnail == value.thumbnail })

            editor.putString(PREF_KEY, gson.toJson(storedData))
            editor.apply()
        }
    }

    fun getBookmarkList() = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            val json = sharedPreferences.getString(key, null)
            val gson = Gson()

            val storedData : List<LOLModel> = gson.fromJson(json, object : TypeToken<List<LOLModel>?>() {}.type) ?: mutableListOf()
            trySend(storedData)
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)

        val json = prefs.getString(PREF_KEY, null)
        val gson = Gson()

        val storedData : List<LOLModel> = gson.fromJson(json, object : TypeToken<List<LOLModel>?>() {}.type) ?: mutableListOf()
        send(storedData)

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.flowOn(Dispatchers.IO).buffer(Channel.UNLIMITED)

    companion object {
        private const val PREF_KEY = "BOOKMARK"
        private var instance: SharedPrefInstance? = null

        fun getInstance() : SharedPrefInstance {
            return instance ?: synchronized(this) {
                    instance ?: SharedPrefInstance().also {
                    instance = it
                }
            }
        }
    }
}
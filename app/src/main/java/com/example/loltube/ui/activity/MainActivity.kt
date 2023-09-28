package com.example.loltube.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.loltube.R
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.databinding.ActivityMainBinding
import com.example.loltube.ui.fragment.MainFragment
import com.example.loltube.util.Utils
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fragmentManager = supportFragmentManager
    private lateinit var transaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.main_fragment_frame, MainFragment())
        transaction.commit()

        lifecycleScope.launch {
            val response = RetrofitInstance.api.getYoutubeTrendVideos(
                regionCode = Utils().getISORegionCode(),
                maxResults = 10
            )
            if (response.isSuccessful) {
                val youtubeVideoInfo = response.body()!!
                Log.d("youtubeapi", youtubeVideoInfo.items!![0].snippet.description)
            }
        }
    }
}
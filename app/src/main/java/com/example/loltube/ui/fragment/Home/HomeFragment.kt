package com.example.loltube.ui.fragment.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.loltube.R
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.databinding.FragmentHomeBinding
import com.example.loltube.model.LOLModel
import com.example.loltube.ui.adapter.HomeCategoryAdapter
import com.example.loltube.ui.adapter.HomeChannelAdapter
import com.example.loltube.ui.adapter.HomePopularAdapter
import com.example.loltube.ui.fragment.VideoDetailFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!


    // nextPageToken 변수
    private var tokenForPopular: String? = null
    private var tokenForCategory: String? = null
    private var tokenForChannel: String? = null

    private var categoryId: String? = null
    private var getQuery: String? = null


    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory())[HomeViewModel::class.java]
    }

    private val popularAdpater by lazy {
        HomePopularAdapter(
            onClickItem = { item ->
            parentFragmentManager.beginTransaction()
                .add(R.id.main_fragment_frame, VideoDetailFragment.newInstance(item))
                .addToBackStack(null).commit()
            }
        )
    }

    private val categoryAdpater by lazy {
        HomeCategoryAdapter(
            onClickItem = { item ->
            parentFragmentManager.beginTransaction()
                .add(R.id.main_fragment_frame, VideoDetailFragment.newInstance(item))
                .addToBackStack(null).commit()
            }
        )
    }
    private val channelAdapter by lazy {
        HomeChannelAdapter(
            onClickItem = { item ->
            parentFragmentManager.beginTransaction()
                .add(R.id.main_fragment_frame, VideoDetailFragment.newInstance(item))
                .addToBackStack(null).commit()
            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        binding.homeSpinner.dismiss()
    }

    private fun initView() = with(binding) {

        recyclerView.adapter = popularAdpater
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(binding.recyclerView)
        recyclerView.addOnScrollListener(onPopularScrollListener)

        recyclerView2.adapter = categoryAdpater
        recyclerView2.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(binding.recyclerView2)
        recyclerView2.addOnScrollListener(onCategoryScrollListener)

        recyclerView3.adapter = channelAdapter
        recyclerView3.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(binding.recyclerView3)
        recyclerView3.addOnScrollListener(onChannelScrollListener)

    }

    private fun initModel() = with(viewModel) {

        listForPopular.observe(viewLifecycleOwner) {
            popularAdpater.submitList(it)
        }
        listForCategory.observe(viewLifecycleOwner) {
            categoryAdpater.submitList(it)
        }
        listForChannel.observe(viewLifecycleOwner) {
            channelAdapter.submitList(it)
        }

        binding.homeSpinner.setOnSpinnerItemSelectedListener<String> { _, _, _, category ->
            when (category) {
                "자동차" -> {
                    setCategoryItem("2")
                    setChannelItem("자동차")
                }

                "스포츠" -> {
                    setCategoryItem("17")
                    setChannelItem("스포츠")
                }

                "음악" -> {
                    setCategoryItem("10")
                    setChannelItem("음악")
                }

                "코미디" -> {
                    setCategoryItem("23")
                    setChannelItem("개그")
                }
            }
        }

    // popular
    lifecycleScope.launch {
        val response = RetrofitInstance.api.getMostPopular(
            videoCategoryId = "20", maxResults = 3
        )
        if (response.isSuccessful) {
            val popularData = response.body()!!

            popularData.items.orEmpty().forEach {
                viewModel.addPopularItem(
                    LOLModel(
                        thumbnail = it.snippet.thumbnails.medium.url,
                        title = it.snippet.title,
                        description = it.snippet.description
                    )
                )
            }
            tokenForPopular = popularData.nextPageToken
            Log.d("choco5732", "nextPageToken = ${tokenForPopular}")
        }
    }

        binding.homeSpinner.selectItemByIndex(0)
    }

    // popular 리스너
    private var onPopularScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollHorizontally(1)) {
                    lifecycleScope.launch {
                        val response = RetrofitInstance.api.getNextMostPopular(
                            videoCategoryId = "20",
                            maxResults = 3,
                            pageToken = "$tokenForPopular"
                        )
                        if (response.isSuccessful) {
                            val popularData = response.body()!!

                            popularData.items.orEmpty().forEach {
                                viewModel.addPopularItem(
                                    LOLModel(
                                        thumbnail = it.snippet.thumbnails.medium.url,
                                        title = it.snippet.title,
                                        description = it.snippet.description
                                    )
                                )
                            }
                            tokenForPopular = popularData.nextPageToken
                        }
                    }
                }
            }
        }


    // 카테고리
    private fun setCategoryItem(category: String) {
        lifecycleScope.launch() {
            val response = RetrofitInstance.api.getCategory(
                videoCategoryId = category, maxResults = 5
            )

            if (response.isSuccessful) {
                val categoryData = response.body()!!
                viewModel.clearCategory()
                categoryData.items.orEmpty().forEach {
                    viewModel.addCategoryItem(
                        LOLModel(
                            thumbnail = it.snippet.thumbnails.medium.url,
                            title = it.snippet.title,
                            description = it.snippet.description
                        )
                    )
                }
                tokenForCategory = categoryData.nextPageToken
                categoryId = category
            }
        }
    }

    // 카테고리 리스너
    private var onCategoryScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollHorizontally(1)) {
                    Log.d("choco5732", "카테고리 끝에 도달")
                    lifecycleScope.launch {
                        val response = RetrofitInstance.api.getNextCategory(
                            videoCategoryId = "$categoryId",
                            maxResults = 10,
                            pageToken = "$tokenForCategory"
                        )
                        if (response.isSuccessful) {
                            val categoryData = response.body()!!

                            categoryData.items.orEmpty().forEach {
                                viewModel.addCategoryItem(
                                    LOLModel(
                                        thumbnail = it.snippet.thumbnails.medium.url,
                                        title = it.snippet.title,
                                        description = it.snippet.description
                                    )
                                )
                            }
                            tokenForCategory = categoryData.nextPageToken
                        }
                    }
                }
            }
        }

    // 채널
    private fun setChannelItem(query: String) {
        lifecycleScope.launch {
            val response = RetrofitInstance.api.getChannel(
                query = query,
                videoType = "channel",
                maxResults = 5,
                regionCode = "KR"
            )

            if (response.isSuccessful) {
                val channelData = response.body()!!
                viewModel.clearChannel()
                channelData.items.orEmpty().forEach {
                    viewModel.addChannelItem(
                        LOLModel(
                            thumbnail = it.snippet.thumbnails.medium.url,
                            title = it.snippet.title,
                            description = it.snippet.description
                        )
                    )
                }
                tokenForChannel = channelData.nextPageToken
                getQuery = query
            }
        }
    }

    // 채널 리스너
    private var onChannelScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollHorizontally(1)) {
                    lifecycleScope.launch {
                        val response = RetrofitInstance.api.getNextChannel(
                            query = "$getQuery",
                            videoType = "channel",
                            maxResults = 10,
                            regionCode = "KR",
                            pageToken = "$tokenForChannel"
                        )
                        if (response.isSuccessful) {
                            val channelData = response.body()!!

                            channelData.items.orEmpty().forEach {
                                viewModel.addChannelItem(
                                    LOLModel(
                                        thumbnail = it.snippet.thumbnails.medium.url,
                                        title = it.snippet.title,
                                        description = it.snippet.description
                                    )
                                )
                            }
                            tokenForChannel = channelData.nextPageToken
                        }
                    }
                }
            }
        }
}


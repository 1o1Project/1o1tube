package com.example.loltube.ui.fragment.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loltube.R
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.databinding.FragmentHomeBinding
import com.example.loltube.model.LOLModel
import com.example.loltube.ui.adapter.HomeAdapterCategory
import com.example.loltube.ui.adapter.HomeAdapterChannel
import com.example.loltube.ui.adapter.HomeAdapterPopular
import com.example.loltube.ui.fragment.VideoDetailFragment
import com.example.loltube.util.Constants.Companion.EXTRA_ITEM
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory())[HomeViewModel::class.java]
    }

    private val popularAdpater by lazy {
        
        HomeAdapterPopular(
            onClickItem = { position, item ->
                parentFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_frame, VideoDetailFragment.newInstance(item))
                    .addToBackStack(null)
                    .commit()
            }
        )
    }

    private val categoryAdpater by lazy {
        HomeAdapterCategory(
            onClickItem = { position, item ->
                parentFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_frame, VideoDetailFragment.newInstance(item))
                    .addToBackStack(null)
                    .commit()
            }
        )
    }
    private val channelAdapter by lazy {
        HomeAdapterChannel(
            onClickItem = { position, item ->
//                val bundle = Bundle()
//                bundle.putParcelable(EXTRA_ITEM, item)
//
//                val videoDetailFragment = VideoDetailFragment()
//                videoDetailFragment.arguments = bundle
//
//                parentFragmentManager.beginTransaction()
//                    .add(R.id.main_fragment_frame, videoDetailFragment)
//                    .addToBackStack(null)
//                    .commit()
                parentFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_frame, VideoDetailFragment.newInstance(item))
                    .addToBackStack(null)
                    .commit()
            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel()
    }

    private fun initView() = with(binding) {
        recyclerView.adapter = popularAdpater
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        recyclerView2.adapter = categoryAdpater
        recyclerView2.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        recyclerView3.adapter = channelAdapter
        recyclerView3.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)


    }

    // 카테고리
    private fun setCategoryItem(category: String) {
        lifecycleScope.launch() {
            val response = RetrofitInstance.api.getYoutubeMostPopular(
                videoCategoryId = category,
                maxResults = 15
            )

            if (response.isSuccessful) {
                val youtubeCategory = response.body()!!
                viewModel.clearCategory()
                youtubeCategory.items.orEmpty().forEach {
                    viewModel.addCategoryItem(
                        LOLModel(
                            thumbnail = it.snippet.thumbnails.medium.url,
                            title = it.snippet.title,
                            description = it.snippet.description
                        )
                    )
                }
            }
        }
    }

    // 채널
    private fun setChannelItem(query: String) {
        lifecycleScope.launch {
            val response = RetrofitInstance.api.getYoutubeChannel(
                query = query,
                videoType = "channel",
                maxResults = 15,
                regionCode = "KR",
                part = "snippet"
            )

            if (response.isSuccessful) {
                val youtubeVideoInfo = response.body()!!
                viewModel.clearChannel()
                youtubeVideoInfo.items.orEmpty().forEach {
                    viewModel.addChannelItem(
                        LOLModel(
                            thumbnail = it.snippet.thumbnails.medium.url,
                            title = it.snippet.title,
                            description = it.snippet.description
                        )
                    )
                }
            }
        }
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

        // most popular
        lifecycleScope.launch {
            val response = RetrofitInstance.api.getYoutubeMostPopular(
                videoCategoryId = "20",
                maxResults = 50
            )
            if (response.isSuccessful) {
                val youtubeVideoInfo = response.body()!!

                youtubeVideoInfo.items.orEmpty().forEach {
                    viewModel.addPopularItem(
                        LOLModel(
                            thumbnail = it.snippet.thumbnails.medium.url,
                            title = it.snippet.title,
                            description = it.snippet.description
                        )
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


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
import androidx.recyclerview.widget.RecyclerView
import com.example.loltube.R
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.databinding.FragmentHomeBinding
import com.example.loltube.model.LOLModel
import com.example.loltube.ui.adapter.HomeAdapterCategory
import com.example.loltube.ui.adapter.HomeAdapterPopular
import com.example.loltube.ui.fragment.VideoDetailFragment
import com.example.loltube.util.Constants.Companion.EXTRA_ITEM
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

//    companion object {
//        fun newInstance(item: LOLModel?) : Fragment {
//            val bundle = Bundle()
//            bundle.putParcelable(EXTRA_ITEM, item)
//
//            val fragment = VideoDetailFragment()
//            fragment.arguments = bundle
//            return fragment
//        }
//    }

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory())[HomeViewModel::class.java]
    }

    private val popularAdpater by lazy {
        HomeAdapterPopular(
            onClickItem = { position, item ->
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_ITEM, item)

                val videoDetailFragment = VideoDetailFragment()
                videoDetailFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_frame, videoDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        )
    }
    private val categoryAdpater by lazy {
        HomeAdapterCategory(
            onClickItem = { position, item ->
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_ITEM, item)

                val videoDetailFragment = VideoDetailFragment()
                videoDetailFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_frame, videoDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        )
    }
    private val channelAdapter by lazy {
        HomeAdapterCategory(
            onClickItem = { position, item ->
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_ITEM, item)

                val videoDetailFragment = VideoDetailFragment()
                videoDetailFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_frame, videoDetailFragment)
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

    private lateinit var category2: String

    private fun setCategoryItem(category: String) {
        lifecycleScope.launch(){
            val response2 = RetrofitInstance.api.getYoutubeMostPopular(
                videoCategoryId = category,
                maxResults = 10
            )

            if (response2.isSuccessful) {
                val youtubeCategory = response2.body()!!
                viewModel.cleareCategoryItem()
                youtubeCategory.items.orEmpty().forEach {
                    viewModel.addCategoryItem(
                        LOLModel(
                            thumbnail = it.snippet.thumbnails.medium.url,
                            title = it.snippet.title
                        )
                    )
                }
            }
        }
    }

    private fun initModel() = with(viewModel) {


        list.observe(viewLifecycleOwner) {
            popularAdpater.submitList(it)
        }
        listForCategory.observe(viewLifecycleOwner) {
            categoryAdpater.submitList(it)
        }

        binding.homeSpinner.setOnSpinnerItemSelectedListener<String> { _, _, _, category ->
            when(category) {
                "자동차" -> setCategoryItem("2")
                "스포츠" -> setCategoryItem("17")
                "음악" -> setCategoryItem("10")
                "코미디" -> setCategoryItem("23")
            }
        }

        lifecycleScope.launch {
            val response = RetrofitInstance.api.getYoutubeMostPopular(
                videoCategoryId = "20",
                maxResults = 50
            )
            if (response.isSuccessful) {
                val youtubeVideoInfo = response.body()!!

                youtubeVideoInfo.items.orEmpty().forEach {
                    viewModel.addHomeItem(
                        LOLModel(
                            thumbnail = it.snippet.thumbnails.medium.url,
                            title = it.snippet.title
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


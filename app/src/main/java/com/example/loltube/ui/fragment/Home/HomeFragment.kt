package com.example.loltube.ui.fragment.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loltube.R
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.databinding.FragmentHomeBinding
import com.example.loltube.model.LOLModel
import com.example.loltube.ui.adapter.HomeAdapter
import com.example.loltube.ui.fragment.VideoDetailFragment
import com.example.loltube.util.Constants.Companion.EXTRA_ITEM
import com.example.loltube.util.Utils
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

    private val homeAdapter by lazy {
        HomeAdapter(
            onClickItem = { position, item ->
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_ITEM, item)
                Log.d("item",item.toString())

                val fragment = VideoDetailFragment()
                fragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_frame,fragment)
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
        recyclerView.adapter = homeAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
//        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView2.adapter = homeAdapter
        recyclerView2.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        recyclerView3.adapter = homeAdapter
        recyclerView3.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)


    }

    private fun initModel() = with(viewModel) {

        binding.homeSpinner.setOnSpinnerItemSelectedListener<String> { _, _, _, category ->
            // use category text with API
        }

        list.observe(viewLifecycleOwner) {
            homeAdapter.submitList(it)
        }
        lifecycleScope.launch {
            val response = RetrofitInstance.api.getYoutubeTrendVideos(
                regionCode = Utils().getISORegionCode(),
                maxResults = 10
            )
            if (response.isSuccessful) {
                val youtubeVideoInfo = response.body()!!

                youtubeVideoInfo.items.orEmpty().forEach {
                    viewModel.addHomeItem(
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


package com.example.loltube.ui.fragment.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loltube.R
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.databinding.FragmentHomeBinding
import com.example.loltube.ui.adapter.HomeAdapter
import com.example.loltube.util.Utils
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory())[HomeViewModel::class.java]
    }

    private val homeAdapter by lazy {
        HomeAdapter(
            onClickItem = { position, item ->
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

        val spinner: Spinner = binding.planetsSpinner// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

    }

    private fun initModel() = with(viewModel) {

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
                        HomeModel(
                            thumnail = it.snippet.thumbnails.medium.url,
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


package com.example.loltube.ui.fragment.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loltube.R
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.databinding.FragmentHomeBinding
import com.example.loltube.ui.adapter.HomeAdapter
import com.example.loltube.ui.fragment.VideoDetailFragment
import com.example.loltube.util.Constants.Companion.EXTRA_ITEM
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

                val bundle = Bundle()
                bundle.putParcelable(EXTRA_ITEM, item)

                VideoDetailFragment().arguments = bundle
                parentFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_frame,VideoDetailFragment())
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


package com.example.loltube.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.data.SharedPrefInstance
import com.example.loltube.databinding.FragmentMyVideoBinding
import com.example.loltube.model.Snippet
import com.example.loltube.ui.adapter.FavoriteListAdapter
import com.example.loltube.util.Utils
import kotlinx.coroutines.launch

class MyVideoFragment : Fragment() {

    private var _binding : FragmentMyVideoBinding? = null
    private val binding : FragmentMyVideoBinding
        get() = _binding!!

    private val adapter by lazy {
        FavoriteListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.mypageFavoriteRv.adapter = adapter
        binding.mypageFavoriteRv.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.mypageFavoriteRv.itemAnimator = null


        lifecycleScope.launch {
            SharedPrefInstance.getInstance().getBookmarkList().asLiveData().observe(viewLifecycleOwner) {
               adapter.setList(it.toMutableList())
            }
            /*repeatOnLifecycle(Lifecycle.State.STARTED) {
                val response = RetrofitInstance.api.getYoutubeTrendVideos(
                    regionCode = Utils().getISORegionCode(),
                    maxResults = 10
                )
                if (response.isSuccessful) {
                    val youtubeVideoInfo = response.body()!!
                    adapter.setList(youtubeVideoInfo.items?.map { it.snippet } as MutableList<Snippet>)
                }
            }*/
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
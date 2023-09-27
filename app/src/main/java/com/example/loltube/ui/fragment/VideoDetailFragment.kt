package com.example.loltube.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.loltube.databinding.FragmentVideoDetailBinding
import com.example.loltube.model.LOLModel
import com.example.loltube.util.Constants.Companion.EXTRA_ITEM

class VideoDetailFragment : Fragment() {

    companion object {
        fun newInstance(item: LOLModel?) : Fragment{
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_ITEM, item)

            val fragment = VideoDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var item : LOLModel? = null

    private var _binding : FragmentVideoDetailBinding? = null
    private val binding : FragmentVideoDetailBinding
        get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoDetailBinding.inflate(inflater, container, false)
        val bundle = arguments
        item = bundle?.getParcelable(EXTRA_ITEM, LOLModel::class.java)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initView() {

        binding.textView2.text = item?.title
        binding.textView3.text = item?.description

        item?.thumbnail?.let { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .into(binding.imageView)
        }


        Log.d("VideoDetailFragment", "Title: ${item?.title}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
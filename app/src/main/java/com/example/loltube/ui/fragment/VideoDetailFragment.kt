package com.example.loltube.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loltube.databinding.FragmentVideoDetailBinding
import com.example.loltube.model.LOLModel
import com.example.loltube.util.Constants
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


    private var _binding : FragmentVideoDetailBinding? = null
    private val binding : FragmentVideoDetailBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val item = arguments?.getString("test")
        val item2 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(EXTRA_ITEM,LOLModel::class.java)
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
        Log.d("choco5732" , "${item2!!.thumbnail}")
        //do something
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
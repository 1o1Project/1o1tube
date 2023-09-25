package com.example.loltube.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loltube.databinding.FragmentMyVideoBinding

class MyVideoFragment : Fragment() {

    private var _binding : FragmentMyVideoBinding? = null
    private val binding : FragmentMyVideoBinding
        get() = _binding!!

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
        //do something
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
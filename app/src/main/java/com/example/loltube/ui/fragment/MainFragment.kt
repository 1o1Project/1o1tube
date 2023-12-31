package com.example.loltube.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loltube.R
import com.example.loltube.databinding.FragmentMainBinding
import com.example.loltube.ui.adapter.ViewPagerFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding : FragmentMainBinding
        get() = _binding!!
    private val adapter by lazy {
        ViewPagerFragmentAdapter(requireActivity())
    }
    private val title = arrayOf("Home", "Shorts", "Search", "MyPage")
    private val tabIcon = arrayOf(
        R.drawable.ic_home,
        R.drawable.ic_my_page,
        R.drawable.ic_search,
        R.drawable.ic_person
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.mainViewpager.adapter = adapter
        binding.mainViewpager.isUserInputEnabled = false

        TabLayoutMediator(binding.mainTabLayout, binding.mainViewpager) { tab, position ->
            tab.text = title[position]
            tab.setIcon(tabIcon[position])
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
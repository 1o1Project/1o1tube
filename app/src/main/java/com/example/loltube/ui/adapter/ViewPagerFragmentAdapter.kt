package com.example.loltube.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loltube.ui.fragment.Home.HomeFragment
import com.example.loltube.ui.fragment.MyVideoFragment
import com.example.loltube.ui.fragment.SearchFragment
import com.example.loltube.ui.fragment.ShortsFragment

class ViewPagerFragmentAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = mutableListOf<Fragment>()

    init {
        fragmentList.add(HomeFragment())
        fragmentList.add(ShortsFragment())
        fragmentList.add(SearchFragment())
        fragmentList.add(MyVideoFragment())
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}
package com.example.newsdaily

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsdaily.fragments.*

class NewsFragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 7
    }

    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0 -> GeneralFragment.getInstance()
            1 -> TechnologyFragment.getInstance()
            2 -> EntertainmentFragment.getInstance()
            3 -> SportsFragment.getInstance()
            4 -> HealthFragment.getInstance()
            5 -> BusinessFragment.getInstance()
            else -> ScienceFragment.getInstance()
        }
    }
}
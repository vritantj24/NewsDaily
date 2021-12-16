package com.example.newsdaily


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.displayOptions= androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.heading)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val pagerAdapter = NewsFragmentPagerAdapter(this)

        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager)
        { tab, position ->
            val tabNames = listOf(getString(R.string.tab_name_general),getString(R.string.tab_name_tech),getString(R.string.tab_name_entertainment),
                getString(R.string.tab_name_sports),getString(R.string.tab_name_health),getString(R.string.tab_name_business),getString(R.string.tab_name_science))
            tab.text = tabNames[position]
        }.attach()

    }
}
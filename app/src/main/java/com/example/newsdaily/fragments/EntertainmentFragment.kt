package com.example.newsdaily.fragments

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsdaily.News
import com.example.newsdaily.NewsItemClicked
import com.example.newsdaily.NewsListAdapter
import com.example.newsdaily.R
import com.example.newsdaily.databinding.EntertainmentFragmentBinding
import com.example.newsdaily.viewModels.EntertainmentViewModel

class EntertainmentFragment : Fragment(), NewsItemClicked {

    companion object {
        @Volatile
        private var INSTANCE: EntertainmentFragment? = null
        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: EntertainmentFragment().also {
                    INSTANCE = it
                }
            }
    }


    private lateinit var viewModel: EntertainmentViewModel
    private lateinit var binding : EntertainmentFragmentBinding
    private lateinit var adapter: NewsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater,R.layout.entertainment_fragment,container,false)


        viewModel = ViewModelProvider(this)[EntertainmentViewModel::class.java]

        getArticles()

        binding.entertainmentRefreshLayout.setOnRefreshListener {

            getArticles()

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                Toast.makeText(context,"Refreshed", Toast.LENGTH_LONG).show()
                binding.entertainmentRefreshLayout.isRefreshing = false
            }, 1532)

        }

        return binding.root
    }

    private fun getArticles()
    {
        viewModel.getData(context,binding.entertainmentProgressBar)
            ?.observe(viewLifecycleOwner,
                { articles ->
                    Log.d("size of the list : ", articles!!.size.toString())

                    adapter = NewsListAdapter(this, articles as ArrayList<News>)
                    binding.entertainmentViewPager.adapter = adapter
                    adapter.notifyDataSetChanged()
                })
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(item.url))
    }
}
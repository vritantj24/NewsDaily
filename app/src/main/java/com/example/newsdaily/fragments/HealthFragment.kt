package com.example.newsdaily.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdaily.News
import com.example.newsdaily.NewsItemClicked
import com.example.newsdaily.NewsListAdapter
import com.example.newsdaily.R
import com.example.newsdaily.databinding.HealthFragmentBinding
import com.example.newsdaily.viewModels.HealthViewModel

class HealthFragment : Fragment(), NewsItemClicked {

    companion object {
        @Volatile
        private var INSTANCE: HealthFragment? = null
        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HealthFragment().also {
                    INSTANCE = it
                }
            }
    }


    private lateinit var viewModel: HealthViewModel
    private lateinit var binding : HealthFragmentBinding
    private lateinit var adapter: NewsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater,R.layout.health_fragment,container,false)

        binding.healthRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.healthRecyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )


        viewModel = ViewModelProvider(this)[HealthViewModel::class.java]

        viewModel.getData(context,binding.healthProgressBar)
            ?.observe(viewLifecycleOwner,
                { articles ->
                    Log.d("size of the list : ", articles!!.size.toString())
                    if (articles.isNotEmpty())
                    {
                        binding.healthProgressBar.visibility=View.GONE
                    }
                    adapter = NewsListAdapter(this, articles as ArrayList<News>)
                    binding.healthRecyclerView.adapter = adapter

                })

        return binding.root
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(item.url))
    }

}
package com.example.newsdaily.fragments

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdaily.News
import com.example.newsdaily.NewsItemClicked
import com.example.newsdaily.NewsListAdapter
import com.example.newsdaily.viewModels.BusinessViewModel
import com.example.newsdaily.R
import com.example.newsdaily.databinding.BusinessFragmentBinding

class BusinessFragment : Fragment(), NewsItemClicked {

    companion object {
        @Volatile
        private var INSTANCE: BusinessFragment? = null
        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BusinessFragment().also {
                    INSTANCE = it
                }
            }
    }


    private lateinit var viewModel: BusinessViewModel
    private lateinit var binding : BusinessFragmentBinding
    private lateinit var adapter: NewsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater,R.layout.business_fragment,container,false)

        binding.businessRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.businessRecyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )


        viewModel = ViewModelProvider(this)[BusinessViewModel::class.java]

        viewModel.getData(context,binding.businessProgressBar)
            ?.observe(viewLifecycleOwner,
                { articles ->
                    Log.d("size of the list : ", articles!!.size.toString())
                    if (articles.isNotEmpty())
                    {
                        binding.businessProgressBar.visibility=View.GONE
                    }
                    adapter = NewsListAdapter(this, articles as ArrayList<News>)
                    binding.businessRecyclerView.adapter = adapter
                })

        return binding.root
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(item.url))
    }

}
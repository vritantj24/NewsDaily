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
import com.example.newsdaily.R
import com.example.newsdaily.databinding.TechnologyFragmentBinding
import com.example.newsdaily.viewModels.TechnologyViewModel

class TechnologyFragment : Fragment(), NewsItemClicked {

    companion object {
        @Volatile
        private var INSTANCE: TechnologyFragment? = null
        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TechnologyFragment().also {
                    INSTANCE = it
                }
            }
    }

    private lateinit var viewModel: TechnologyViewModel
    private lateinit var binding : TechnologyFragmentBinding
    private lateinit var adapter: NewsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater,R.layout.technology_fragment,container,false)

        binding.technologyRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.technologyRecyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )


        viewModel = ViewModelProvider(this)[TechnologyViewModel::class.java]

        viewModel.getData(context,binding.technologyProgressBar)
            ?.observe(viewLifecycleOwner,
                { articles ->
                    Log.d("size of the list : ", articles!!.size.toString())
                    if (articles.isNotEmpty())
                    {
                        binding.technologyProgressBar.visibility=View.GONE
                    }
                    adapter = NewsListAdapter(this, articles as ArrayList<News>)
                    binding.technologyRecyclerView.adapter = adapter
                })

        return binding.root
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(item.url))
    }

}
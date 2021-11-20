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
import com.example.newsdaily.viewModels.EntertainmentViewModel
import com.example.newsdaily.R
import com.example.newsdaily.databinding.EntertainmentFragmentBinding

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

        binding.entertainmentRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.entertainmentRecyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )


        viewModel = ViewModelProvider(this)[EntertainmentViewModel::class.java]

        viewModel.getData(context,binding.entertainmentProgressBar)
            ?.observe(viewLifecycleOwner,
                { articles ->
                    Log.d("size of the list : ", articles!!.size.toString())
                    if (articles.isNotEmpty())
                    {
                        binding.entertainmentProgressBar.visibility=View.GONE
                    }
                    adapter = NewsListAdapter(this, articles as ArrayList<News>)
                    binding.entertainmentRecyclerView.adapter = adapter
                })

        return binding.root
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(item.url))
    }
}
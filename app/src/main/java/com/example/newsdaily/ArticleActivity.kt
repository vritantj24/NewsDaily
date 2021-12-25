package com.example.newsdaily

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdaily.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity(),NewsItemClicked,NewsItemShareClicked {

    private lateinit var viewModel: ArticleViewModel
    private lateinit var adapter: NewsListAdapter
    private lateinit var binding : ActivityArticleBinding
    private lateinit var category: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_article)
        supportActionBar?.displayOptions= androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.heading)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)

        binding.articleRv.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        binding.articleRv.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )

        val cat  = intent.getStringExtra("category")?.lowercase()
        if (cat != null) {
            category=cat
        }

        viewModel = ViewModelProvider(this)[ArticleViewModel::class.java]

        getArticles()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
        {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getArticles()
    {
        viewModel.getData(this,binding.articleProgress, category)?.observe(this,
        {  articles ->
            Log.d("size of the list : ", articles!!.size.toString())

            adapter = NewsListAdapter(this, articles as ArrayList<News>,this)
            binding.articleRv.adapter=adapter
            //binding.general_news.adapter=newsAdapter
            adapter.notifyDataSetChanged()
        })
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

    override fun onItemShareClicked(item: News) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this news I read from News Daily App ${item.url} ")
        val chooser= Intent.createChooser(intent,"Share this news using...")
        startActivity(chooser)
    }
}
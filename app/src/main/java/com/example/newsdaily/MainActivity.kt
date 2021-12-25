package com.example.newsdaily


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsdaily.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NewsItemClicked,NewsCategoryClicked,NewsItemShareClicked{

    private lateinit var viewModel: MainViewModel
    private lateinit var newsAdapter: NewsListAdapter
    private lateinit var binding : ActivityMainBinding
    private lateinit var categoryList : ArrayList<Category>
    private lateinit var categoryAdapter: NewsCategoryAdapter
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        supportActionBar?.displayOptions= androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.heading)

        binding.generalNews.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.generalNews.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        binding.categoryCards.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        drawerLayout=findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_contact -> {
                    val address = arrayOf(getString(R.string.developer_email))
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.putExtra(Intent.EXTRA_EMAIL,address)
                    intent.data = Uri.parse("mailto:")
                    if(intent.resolveActivity(packageManager)!=null)
                    {
                        startActivity(intent)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_open_24)

        categoryList = ArrayList()
        categoryList.add(Category(getString(R.string.name_tech),R.drawable.technology))
        categoryList.add(Category(getString(R.string.name_business),R.drawable.business))
        categoryList.add(Category(getString(R.string.name_health),R.drawable.health))
        categoryList.add(Category(getString(R.string.name_entertainment),R.drawable.entertainment))
        categoryList.add(Category(getString(R.string.name_sports),R.drawable.sports))
        categoryList.add(Category(getString(R.string.name_science),R.drawable.science))

        categoryAdapter = NewsCategoryAdapter(this,categoryList)
        binding.categoryCards.adapter=categoryAdapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        getArticles()

    }

    private fun getArticles()
    {
        viewModel.getData(this,binding.progress)?.observe(this, Observer
        {  articles ->
            Log.d("size of the list : ", articles!!.size.toString())

            newsAdapter = NewsListAdapter(this, articles as ArrayList<News>,this)
            binding.generalNews.adapter=newsAdapter
            //binding.general_news.adapter=newsAdapter
            newsAdapter.notifyDataSetChanged()
        })
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            true
        } else
        super.onOptionsItemSelected(item)
    }

    override fun onItemShareClicked(item: News) {
        val intent =Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this news I read from News Daily App ${item.url} ")
        val chooser= Intent.createChooser(intent,"Share this news using...")
        startActivity(chooser)
    }

    override fun onItemClicked(item: Category) {
        val intent = Intent(this,ArticleActivity::class.java)
        intent.putExtra("category",item.title1)
        startActivity(intent)
    }


}
package com.example.newsdaily


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),NewsItemClicked, SharedPreferences.OnSharedPreferenceChangeListener{

    private var category = "general"
    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.displayOptions=ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.heading)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )

        setupSharedPreferences()

        mAdapter = NewsListAdapter(this)
        MySingleton.getInstance(this).fetchData(mAdapter,this,progressBar,category)
        recyclerView.adapter=mAdapter

    }

    private fun setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)

        loadCategoryFromPreferences(sharedPreferences)

        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    private fun loadCategoryFromPreferences(sharedPreferences: SharedPreferences?)
    {
        if (sharedPreferences != null) {
            category =
                sharedPreferences.getString(getString(R.string.pref_news_key),
                    getString(R.string.pref_news_general_value)).toString()
        }
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key==getString(R.string.pref_news_key))
        {
            loadCategoryFromPreferences(sharedPreferences)
            MySingleton.getInstance(this).fetchData(mAdapter,this,progressBar,category)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        val inflater = menuInflater

        inflater.inflate(R.menu.news_menu, menu)
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            val startSettingsActivity = Intent(this, SettingsActivity::class.java)
            startActivity(startSettingsActivity)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
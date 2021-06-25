package com.example.newsdaily

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


class MySingleton constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: MySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    private val key = "a7ca283637ad4f0fb7c6218fa00fb292"
    private val api_key_tag = "apikey"
    private val category_tag = "category"
    private val headlineurl = "https://newsapi.org/v2/top-headlines?country=in"

    private val requestQueue = Volley.newRequestQueue(context.applicationContext)


    fun fetchData(adapter: NewsListAdapter,context: Context,progressBar: ProgressBar,
                  news_category : String) {
        progressBar.visibility=View.VISIBLE

        val url:String = headlineurl


        val baseuri:Uri = Uri.parse(url)
        val builder : Uri.Builder? = baseuri.buildUpon()


        if (builder != null) {
            builder.appendQueryParameter(category_tag,news_category)
            builder.appendQueryParameter(api_key_tag,key)
        }

        val ur = builder.toString()

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            ur,
            null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                progressBar.visibility=View.GONE
                adapter.updateNews(newsArray)
            },
            {
                progressBar.visibility=View.GONE
                Toast.makeText(context,"Error Fetching the News! Please Check Your Internet",Toast.LENGTH_LONG).show()
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }

        }

        requestQueue.add(jsonObjectRequest)
    }
}
package com.example.newsdaily

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.MutableMap
import kotlin.collections.set

class ArticleViewModel : ViewModel() {

    private var newsList: MutableLiveData<List<News>>? = null
    private var mQueue: RequestQueue? = null
    private val articleList = ArrayList<News>()
    private var key = "a7ca283637ad4f0fb7c6218fa00fb292"
    private val apiKeyTag = "apiKey"
    private val categoryTag = "category"
    private val headlineUrl = "https://newsapi.org/v2/top-headlines?country=in"
    private val pageTag = "pageSize"
    private val pageSize = "100"

    private lateinit var title : String
    private lateinit var author : String
    private lateinit var articleUrl : String
    private lateinit var imageUrl : String
    private lateinit var content : String
    private lateinit var newsAgency : String


    fun getData(context: Context?, progressBar: ProgressBar,category : String): LiveData<List<News>>? {
        if (newsList == null) {
            newsList = MutableLiveData<List<News>>()

            if (context != null) {
                fetchData(context,progressBar,category)
            }
        }
        return newsList
    }

    private fun fetchData(context: Context, progressBar: ProgressBar,
                          news_category : String)
    {
        mQueue = MySingleton.getInstance(context).getRequestQueue()
        progressBar.visibility= View.VISIBLE

        val url:String = headlineUrl


        val baseUri: Uri = Uri.parse(url)
        val builder : Uri.Builder? = baseUri.buildUpon()


        if (builder != null) {
            builder.appendQueryParameter(categoryTag,news_category)
            builder.appendQueryParameter(apiKeyTag,key)
            builder.appendQueryParameter(pageTag,pageSize)
        }

        val ur = builder.toString()

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            ur,
            null,
            {
                val status = it.getString("status")

                if(status=="error")
                {
                    key="752262aaa61b43bc80a60a8a6e720879"
                    fetchData(context,progressBar, news_category)
                }

                val newsJsonArray = it.getJSONArray("articles")

                for(i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    title = newsJsonObject.getString("title")
                    val sourceJSONObject = newsJsonObject.getJSONObject("source")
                    newsAgency = sourceJSONObject.getString("name")
                    author = newsJsonObject.getString("author")
                    articleUrl = newsJsonObject.getString("url")
                    imageUrl = newsJsonObject.getString("urlToImage")
                    val contentString = newsJsonObject.getString("content")
                    if(contentString!="null")
                    {
                        content = contentString.dropLast(15)
                        content = "$content...................."
                    }
                    else
                    {
                        content = "No details found. Please head over to read the full article."
                    }
                    if(author=="null")
                    {
                        author="Unknown"
                    }

                    val news = News(title, author,newsAgency, articleUrl, imageUrl,content)


                    articleList.add(news)
                }

                newsList?.value = articleList
                progressBar.visibility= View.GONE
            },
            {
                progressBar.visibility= View.GONE
                Toast.makeText(context,"Error Fetching the News! Please Check Your Internet", Toast.LENGTH_LONG).show()
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }

        }

        mQueue!!.add(jsonObjectRequest)
    }
}
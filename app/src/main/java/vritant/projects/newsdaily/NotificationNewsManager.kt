package vritant.projects.newsdaily

import android.content.Context
import android.content.SharedPreferences

class NotificationNewsManager( context: Context) {

    private var sharedPreferences : SharedPreferences = context.getSharedPreferences("news",0)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()


    fun setNews(article : News)
    {
        editor.putString("title",article.title)
        editor.putString("author",article.author)
        editor.putString("newsAgency",article.newsAgency)
        editor.putString("url",article.url)
        editor.putString("imageUrl",article.imageUrl)
        editor.putString("content",article.content)
        editor.commit()
    }

    fun getNews() : News
    {
        return News(sharedPreferences.getString("title","").toString(),
            sharedPreferences.getString("author","").toString(),
        sharedPreferences.getString("newsAgency","").toString(),
            sharedPreferences.getString("url","").toString(),
            sharedPreferences.getString("imageUrl","").toString(),
            sharedPreferences.getString("content","").toString(),
            )
    }
}
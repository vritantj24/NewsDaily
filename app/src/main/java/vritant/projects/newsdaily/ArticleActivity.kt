package vritant.projects.newsdaily

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.browser.customtabs.CustomTabsIntent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_article.*

class ArticleActivity : AppCompatActivity(), NewsItemClicked,
    NewsItemShareClicked {

    private lateinit var article : News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        supportActionBar?.displayOptions= androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.heading)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)


        val bundle = intent.getBundleExtra("article")
        if (bundle != null) {
            article = News(
                bundle.getString("articleTitle").toString(),
                bundle.getString("articleAuthor").toString(),
                bundle.getString("articleNewsAgency").toString(),
                bundle.getString("articleUrl").toString(),
                bundle.getString("articleImageURL").toString(),
                bundle.getString("articleContent").toString()
            )
        }

        author.text =  article.author
        title_tv.text = article.title
        news_agency.text = article.newsAgency

        Glide.with(image)
            .asBitmap()
            .load(article.imageUrl)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                    image.setImageBitmap(resource)
                }

                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    image.setImageDrawable(getDrawable(R.drawable.news_error_image))
                }

                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onLoadCleared(placeholder: Drawable?) {
                    image.setImageDrawable(getDrawable(R.drawable.news_error_image))
                }
            })

        content.text = article.content

        val builder =  Uri.Builder()
        builder.appendPath("https://google.com")
            .build()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
        {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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
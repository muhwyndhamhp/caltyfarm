package com.caltyfarm.caltyfarm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.ARTICLE_ID_CODE
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.activity_article_read.*
import ru.noties.markwon.Markwon

class ArticleReadActivity : AppCompatActivity() {

    lateinit var viewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_read)

        val factory = InjectorUtils.provideArticleViewModelFactory(this, true, intent.getStringExtra(ARTICLE_ID_CODE))
        viewModel = ViewModelProviders.of(this, factory).get(ArticleViewModel::class.java)

        viewModel.singleArticle.observe(this, Observer {
            if (it != null){
                Glide.with(this@ArticleReadActivity).load(it.imageUrl).into(iv_article_read)
                Markwon.setMarkdown(tv_title_read, "**${it.title}**")
                Markwon.setMarkdown(tv_body_read, it.body)
            }
        })
    }
}

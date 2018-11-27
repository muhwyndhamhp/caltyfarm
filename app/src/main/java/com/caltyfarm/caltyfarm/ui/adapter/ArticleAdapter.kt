package com.caltyfarm.caltyfarm.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Article
import com.caltyfarm.caltyfarm.ui.ArticleReadActivity
import com.caltyfarm.caltyfarm.ui.MainActivity
import com.caltyfarm.caltyfarm.utils.ARTICLE_ID_CODE
import com.caltyfarm.caltyfarm.utils.LoggingListener
import com.caltyfarm.caltyfarm.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.item_article.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ArticleAdapter(val context: Context,val lifecycleOwner: LifecycleOwner, val viewModel: ArticleViewModel) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(position: Int, article: Article, context: Context, viewModel: ArticleViewModel) {
            Glide.with(itemView.iv_article.context).load(article.imageUrl).listener(LoggingListener<Drawable>()).into(itemView.iv_article)
            itemView.tv_title.text = article.title
            itemView.tv_category.text = article.categoryName

            itemView.onClick {
                val intent = Intent(context, ArticleReadActivity::class.java)
                intent.putExtra(ARTICLE_ID_CODE, article.id)
                (context as Activity).startActivity(intent)
            }
        }

    }

    lateinit var articleList: MutableList<Article>

    init {
        viewModel.articleList.observe(lifecycleOwner, Observer {
            articleList = it
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_article,
                    parent,
                    false
                )
        )

    override fun getItemCount() = if(::articleList.isInitialized)articleList.size else 0

    override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
        holder.bindView(position, articleList[position], context, viewModel)
    }

}
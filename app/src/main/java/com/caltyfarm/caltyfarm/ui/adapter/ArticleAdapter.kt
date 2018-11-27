package com.caltyfarm.caltyfarm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Article
import com.caltyfarm.caltyfarm.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleAdapter(val context: Context,val lifecycleOwner: LifecycleOwner, val viewModel: ArticleViewModel) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(position: Int, article: Article, context: Context, viewModel: ArticleViewModel) {
            Glide.with(itemView.iv_article.context).load(article.imageUrl).into(itemView.iv_article)
            itemView.tv_title.text = article.title
            itemView.tv_category.text = article.categoryName
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
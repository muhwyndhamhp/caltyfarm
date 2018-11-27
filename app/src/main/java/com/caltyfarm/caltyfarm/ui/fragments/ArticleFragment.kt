package com.caltyfarm.caltyfarm.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.ui.adapter.ArticleAdapter
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_article.*
import org.jetbrains.anko.toast

class ArticleFragment(): Fragment(){
    companion object {
        fun newInstance() = ArticleFragment()
    }

    lateinit var viewModel: ArticleViewModel
    lateinit var adapter: ArticleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val factory = InjectorUtils.provideArticleViewModelFactory(context!!)
        viewModel = ViewModelProviders.of(this, factory).get(ArticleViewModel::class.java)

        prepareRecyclerView()

        viewModel.errorMessage.observe(this, Observer {
            if(it != "") (context as Activity).toast(it)
        })

        viewModel.updatePosition.observe(this, Observer {
            if(it != null) adapter.notifyItemChanged(it)

        })

        viewModel.addPosition.observe(this, Observer {
            if(it != null) adapter.notifyItemInserted(it)
        })

        viewModel.deletePosition.observe(this, Observer {
            if(it != null) adapter.notifyItemRemoved(it)
        })

        prepareRecyclerView()

    }

    private fun prepareRecyclerView() {
        rv_article.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = ArticleAdapter(context!!, this, viewModel)
        rv_article.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}
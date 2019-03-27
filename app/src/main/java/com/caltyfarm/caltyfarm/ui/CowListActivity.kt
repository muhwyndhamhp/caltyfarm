package com.caltyfarm.caltyfarm.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.ui.adapter.CowListAdapter
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.USER_DATA_KEY
import com.caltyfarm.caltyfarm.viewmodel.CowListViewModel
import kotlinx.android.synthetic.main.activity_cow_list.*
import org.jetbrains.anko.toast

class CowListActivity : AppCompatActivity() {

    lateinit var viewModel: CowListViewModel
    lateinit var adapter: CowListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cow_list)
        viewModel = ViewModelProviders.of(
            this, InjectorUtils.provideCowListViewModelFactory(
                intent.getSerializableExtra(USER_DATA_KEY) as User,
                this
            )
        ).get(CowListViewModel::class.java)

        prepareRecyclerView()
        prepareToolbar()

        viewModel.errorMessage.observe(this, Observer { toast(it) })
        viewModel.addPosition.observe(this, Observer { if (it != null) adapter.notifyItemInserted(it) })
        viewModel.changePosition.observe(this, Observer { if (it != null) adapter.notifyItemChanged(it) })
        viewModel.removePosition.observe(this, Observer { if (it != null) adapter.notifyItemRemoved(it) })
        viewModel.cowIntent.observe(this, Observer { if (it != null) startActivity(it) })

    }

    private fun prepareToolbar() {
        setSupportActionBar(toolbar_cow_list)
        val upArrow = resources.getDrawable(R.drawable.ic_qiscus_back)
        supportActionBar!!.setHomeAsUpIndicator(upArrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun prepareRecyclerView() {
        rv_cow_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = CowListAdapter(viewModel, this)
        rv_cow_list.adapter = adapter
        rv_cow_list.scheduleLayoutAnimation()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item!!.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }
}

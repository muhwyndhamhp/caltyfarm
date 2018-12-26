package com.caltyfarm.caltyfarm.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.ui.adapter.RoomListAdapter
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.USER_DATA_KEY
import com.caltyfarm.caltyfarm.viewmodel.RoomListViewModel
import kotlinx.android.synthetic.main.activity_room_list.*
import org.jetbrains.anko.toast

class RoomListActivity : AppCompatActivity() {

    lateinit var viewModel: RoomListViewModel
    lateinit var adapter: RoomListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_list)

        viewModel = ViewModelProviders.of(
            this, InjectorUtils.provideRoomListVieModelFactory(
                intent.getSerializableExtra(
                    USER_DATA_KEY
                ) as User,
                this
            )
        )
            .get(RoomListViewModel::class.java)

        prepareRecyclerView()
        prepareToolbar()

        viewModel.errorMessage.observe(this, Observer { toast(it) })
        viewModel.addPosition.observe(this, Observer { if (it != null) adapter.notifyItemInserted(it) })
        viewModel.changePosition.observe(this, Observer { if (it != null) adapter.notifyItemChanged(it) })
        viewModel.removePosition.observe(this, Observer { if (it != null) adapter.notifyItemRemoved(it) })
        viewModel.chatIntent.observe(this, Observer { if (it != null) startActivity(it) })

    }

    private fun prepareToolbar() {
        setSupportActionBar(toolbar_room_list)
        val upArrow = resources.getDrawable(R.drawable.ic_qiscus_back)
        supportActionBar!!.setHomeAsUpIndicator(upArrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if(item!!.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun prepareRecyclerView() {
        qs_rv_room_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = RoomListAdapter(viewModel, Glide.with(this@RoomListActivity), this)
        qs_rv_room_list.adapter = adapter
        qs_rv_room_list.scheduleLayoutAnimation()
    }
}

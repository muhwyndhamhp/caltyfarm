package com.caltyfarm.caltyfarm.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.utils.LoggingListener
import com.caltyfarm.caltyfarm.viewmodel.RoomListViewModel
import kotlinx.android.synthetic.main.activity_room_list.view.*
import kotlinx.android.synthetic.main.item_room_list.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class RoomListAdapter(
    private val viewModel: RoomListViewModel,
    private val glide: RequestManager,
    lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RoomListAdapter.ViewHolder>() {

    private lateinit var roomList: List<User>

    init {
        viewModel.friendList.observe(lifecycleOwner, Observer {
            roomList = it
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_room_list,
                    parent,
                    false
                )
        )

        vh.apply {
            itemView.onClick {
                viewModel.createQiscusChatRoom(adapterPosition)
            }
        }
        return vh

    }

    override fun getItemCount(): Int = roomList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(roomList[position], position, glide)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(
            qiscusChatRoom: User,
            position: Int,
            glide: RequestManager
        ) {
            glide.load(
                if(qiscusChatRoom.profileUrl != "") qiscusChatRoom.profileUrl
                else "https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909__340.png"
            ).listener(LoggingListener<Drawable>()).into(itemView.iv_chat_avatar)
            itemView.tV_chat_name.text = qiscusChatRoom.name
        }

    }

}
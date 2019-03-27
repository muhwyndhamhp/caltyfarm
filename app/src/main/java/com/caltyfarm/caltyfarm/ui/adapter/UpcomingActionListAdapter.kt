package com.caltyfarm.caltyfarm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.ui.reminder.Reminder
import com.caltyfarm.caltyfarm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.recycle_items.view.*


class UpcomingActionListAdapter(
    reminderList: List<Reminder>,
    lifecycleOwner: LifecycleOwner,
    val viewModel: MainViewModel
) : RecyclerView.Adapter<UpcomingActionListAdapter.ViewHolder>() {

    private var roomList = reminderList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.recycle_items,
                    parent,
                    false
                )
        )
        return vh
    }

    override fun getItemCount(): Int = roomList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(roomList[position], position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(reminder: Reminder, position: Int) {
            itemView.apply {
                val mDrawableBuilder = TextDrawable.builder()
                    .buildRound(reminder.title.split("")[0], ColorGenerator.DEFAULT.randomColor)
                thumbnail_image.setImageDrawable(mDrawableBuilder)
                recycle_title.text = reminder.title
                recycle_date_time.text = "${reminder.date} ${reminder.time}"
                if (reminder.repeat == "true") {
                    recycle_repeat_info.text = "Every ${reminder.repeatNo} ${reminder.repeatType}(s)"
                } else if (reminder.repeat == "false") {
                    recycle_repeat_info.text = "Repeat Off"
                }
                active_image.visibility = View.GONE
            }
        }

    }

}
package com.caltyfarm.caltyfarm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Cow
import com.caltyfarm.caltyfarm.viewmodel.CowListViewModel
import kotlinx.android.synthetic.main.item_cow_list.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class CowListAdapter(val viewModel: CowListViewModel, val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<CowListAdapter.ViewHolder>(){


    private lateinit var cowList : List<Cow>

    init {
        viewModel.cowList.observe(lifecycleOwner, Observer { cowList ->
            this.cowList = cowList
        })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh = ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_cow_list,
                    parent,
                    false
                )
        )

        vh.apply {
            itemView.onClick {
                viewModel.showCowData(adapterPosition)
            }
        }
        return vh
    }

    override fun getItemCount(): Int = cowList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(cowList[position], position)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(cow: Cow, position: Int) {
            itemView.apply {
                tv_cow_id.text = cow.id.toString()
                if(cow.isHealthy) {
                    tv_cow_healthy.visibility = View.VISIBLE
                    tv_cow_not_healthy.visibility = View.INVISIBLE
                } else {
                    tv_cow_healthy.visibility = View.INVISIBLE
                    tv_cow_not_healthy.visibility = View.VISIBLE
                }
            }
        }

    }

}
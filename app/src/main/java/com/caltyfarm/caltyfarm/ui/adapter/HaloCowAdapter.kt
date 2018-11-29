package com.caltyfarm.caltyfarm.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Vet
import com.caltyfarm.caltyfarm.ui.HaloCowsActivity
import com.caltyfarm.caltyfarm.utils.LoggingListener
import com.caltyfarm.caltyfarm.utils.REQUEST_PHONE_CODE
import com.caltyfarm.caltyfarm.viewmodel.HaloCowsViewModel
import kotlinx.android.synthetic.main.item_halo_cow.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.NumberFormat
import java.util.*

class HaloCowAdapter(val context: Context, lifecycleOwner: LifecycleOwner, val viewModel: HaloCowsViewModel) :
    RecyclerView.Adapter<HaloCowAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(
            vet: Vet,
            context: Context) {
            itemView.apply {
                tv_vet_name.text = vet.name
                tv_vet_job.text = vet.job
                tv_vet_exp.text = "Pengalaman ${vet.exp} Tahun"
                tv_vet_cost.text = NumberFormat
                    .getCurrencyInstance(Locale("in", "ID"))
                    .format(vet.price)
                tv_vet_time.text = "Tersedia pukul ${vet.startTime} - ${vet.endTime} WIB"
                tv_vet_school.text = vet.school
                tv_vet_affiliate.text = vet.hospital
            }
            if (vet.profileUrl != "") Glide.with(context).load(vet.profileUrl).listener(LoggingListener<Drawable>()).into(
                itemView.iv_vet_profile
            )
            itemView.bt_halo_cow_request.onClick {
                itemView.tv_vet_phone.visibility = View.VISIBLE
                setPhoneClickListener(context)
            }
        }

        private fun setPhoneClickListener(context: Context) {
            (context as HaloCowsActivity).phoneNumber = itemView.tv_vet_phone.text as String
            itemView.tv_vet_phone.onClick {
                if(setupPermission(context))
                {
                    (context as Activity).startActivity(
                        Intent(
                            Intent.ACTION_CALL,
                            Uri.parse("tel:${itemView.tv_vet_phone.text}")
                        )
                    )
                }
                else {
                    ActivityCompat.requestPermissions(
                        (context as Activity),
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        REQUEST_PHONE_CODE
                    )

                }
            }
        }

        private fun setupPermission(context: Context): Boolean {

            val permission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)

            return permission == PackageManager.PERMISSION_GRANTED
        }

    }

    lateinit var vetList: MutableList<Vet>

    init {
        viewModel.vetList.observe(lifecycleOwner, Observer {
            vetList = it
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HaloCowAdapter.ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_halo_cow,
                    parent,
                    false
                )
        )

    override fun getItemCount() = if (::vetList.isInitialized) vetList.size else 0

    override fun onBindViewHolder(holder: HaloCowAdapter.ViewHolder, position: Int) {
        holder.bindView(vetList[position], context)
    }
}


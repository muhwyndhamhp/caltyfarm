package com.caltyfarm.caltyfarm.ui.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Vet
import com.caltyfarm.caltyfarm.ui.HaloCowsActivity
import com.caltyfarm.caltyfarm.utils.LoggingListener
import kotlinx.android.synthetic.main.fragment_govetdatavet.*
import kotlinx.android.synthetic.main.item_halo_cow.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class GovetDataVetFragment(): Fragment(){
    
    companion object {
        
        private lateinit var vetData : Vet
        private lateinit var polyDistance: String
        private lateinit var polyDuration: String

        fun newInstance(
            vetData: Vet,
            it: String,
            polyDuration: String?
        ) : GovetDataVetFragment{
            this.vetData = vetData
            this.polyDistance = it
            this.polyDuration = polyDuration!!
            return GovetDataVetFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = 
        inflater.inflate(R.layout.fragment_govetdatavet, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            tv_vet_name.text = vetData.name
            tv_vet_job.text = vetData.job
            tv_vet_exp.text = "Pengalaman ${vetData.exp} Tahun"
            tv_vet_time.text = "Tersedia pukul ${vetData.startTime} - ${vetData.endTime} WIB"
            tv_vet_school.text = vetData.school
            tv_vet_affiliate.text = vetData.hospital
            tv_vet_distance.text = if(polyDistance.toLong() > 1000) "${polyDistance.toInt()/1000} Km dari Anda" else "${polyDistance.toInt()} m dari Anda"
        }
        if (vetData.profileUrl != "") Glide.with(context!!).load(vetData.profileUrl).listener(LoggingListener<Drawable>()).into(
            view.iv_vet_profile
        )
        view.bt_halo_cow_request.onClick {
            view.tv_vet_phone.visibility = View.VISIBLE
            setPhoneClickListener(context!!)
        }
    }

    private fun setPhoneClickListener(context: Context) {
        (context as HaloCowsActivity).phoneNumber = view!!.tv_vet_phone.text as String
        view!!.tv_vet_phone.onClick {
            activity!!.toast("Clicked!")
        }
    }
}
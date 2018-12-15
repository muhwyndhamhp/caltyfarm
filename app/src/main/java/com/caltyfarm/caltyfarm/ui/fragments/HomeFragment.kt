package com.caltyfarm.caltyfarm.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.ui.CaltyShopActivity
import com.caltyfarm.caltyfarm.ui.HaloCowsActivity
import com.caltyfarm.caltyfarm.ui.GoVetActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.sdk27.coroutines.onClick

class HomeFragment(): Fragment(){
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layout_halo_cow.onClick { startActivity(Intent(context, HaloCowsActivity::class.java)) }
        layout_caltyshop.onClick { startActivity(Intent(context, CaltyShopActivity::class.java))}
        layout_govet.onClick { startActivity(Intent(context, GoVetActivity::class.java)) }
        layout_mycows.onClick { activity!!.alert("Saat ini fitur My Cows masih dalam tahap pengembangan!", "Fitur Dalam Pengembangan!"){
            okButton {}
        }.build().show() }
    }
}
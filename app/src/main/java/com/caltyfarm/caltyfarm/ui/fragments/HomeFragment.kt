package com.caltyfarm.caltyfarm.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.ui.HaloCowsActivity
import kotlinx.android.synthetic.main.fragment_home.*
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
    }
}
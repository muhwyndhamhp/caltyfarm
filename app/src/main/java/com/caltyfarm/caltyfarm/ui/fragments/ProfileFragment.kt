package com.caltyfarm.caltyfarm.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.ui.ProfileEditActivity
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class ProfileFragment(): Fragment(){
    companion object {
        fun newInstance() = ProfileFragment()
    }


    lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val factory = InjectorUtils.provideProfileViewModelFactory(context!!)
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)

        viewModel.user.observe(this, Observer {
            view.tv_name.text = if(it.name != null && it.name != "") {it.name} else {"User Calty"}
            view.tv_phone.text = it.phone
            view.tv_email.text = if(it.email!= null && it.email != "") it.email else "user@caltyfarm.com"
            if(it.profileUrl!= null && it.profileUrl != "") Glide.with(context!!).load(it.profileUrl).into(view.iv_profile)
            else Glide.with(context!!).load(R.drawable.images).into(view.iv_profile)
            view.progress_horizontal.visibility = View.GONE
        })

        viewModel.errorMessage.observe(this, Observer {
            if(it != "") context!!.toast(it)
        })

        cv_edit_or_add.onClick { navigateToProfileEdit() }
    }

    private fun navigateToProfileEdit() {
        startActivity(Intent(context!!, ProfileEditActivity::class.java))
    }
}
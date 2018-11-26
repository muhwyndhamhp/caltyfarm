package com.caltyfarm.caltyfarm.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_profile_edit.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileEditActivity : AppCompatActivity() {

    lateinit var viewModel: ProfileViewModel

    private var isInitiated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        val factory = InjectorUtils.provideProfileViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)

        initEdittext()
    }

    private fun initEdittext() {
        viewModel.user.observe(this, androidx.lifecycle.Observer { userData ->
            if(!isInitiated && userData != null){
                et_name_edit.setText(userData.name)
                et_date_edit.setText(convertDate(userData.birthdate!!))
                et_phone_edit.setText(userData.phone)
                et_email_edit.setText(userData.email)

                ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item)
                    .also {
                        it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner_gender_edit.adapter = it
                    }

                when (userData.gender) {
                    0 -> spinner_gender_edit.setSelection(0)
                    1 -> spinner_gender_edit.setSelection(1)
                    2 -> spinner_gender_edit.setSelection(2)
                }
                isInitiated = true
            }
        })


    }

    @SuppressLint("SimpleDateFormat")
    private fun convertDate(birthdate: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date(birthdate)
        return dateFormat.format(date)
    }
}

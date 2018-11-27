package com.caltyfarm.caltyfarm.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.ProfileViewModel
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.activity_profile_edit.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

class ProfileEditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        et_date_edit.setText("$dayOfMonth/${setMonth(monthOfYear)}/$year")
    }

    private fun setMonth(month: Int): String {
        if(month < 9) {
            return "0${month+1}"
        } else {
            return (month+1).toString()
        }
    }

    lateinit var viewModel: ProfileViewModel

    private var isInitiated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        val factory = InjectorUtils.provideProfileViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)

        initEdittext()

        et_date_edit.onClick {
            SpinnerDatePickerDialogBuilder()
                .context(this@ProfileEditActivity)
                .callback(this@ProfileEditActivity)
                .spinnerTheme(R.style.NumberPickerStyle)
                .showTitle(false)
                .showDaySpinner(true)
                .defaultDate(2018,1,1)
                .build()
                .show()
        }

        bt_profile_edit_save.onClick {
            viewModel.user.value!!.name = et_name_edit.text.toString()
            viewModel.user.value!!.birthdate = convertDate(et_date_edit.text.toString())
            viewModel.user.value!!.gender = spinner_gender_edit.selectedItemPosition
            viewModel.user.value!!.email = et_email_edit.text.toString()
            viewModel.updateUser()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        isInitiated = false
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertDate(birthdate: String): Long {
        val date = SimpleDateFormat("dd/MM/yyyy").parse(birthdate)
        return date.time/1000
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

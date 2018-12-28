package com.caltyfarm.caltyfarm.ui.inputcow

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.ActionHistory
import kotlinx.android.synthetic.main.activity_add_action_history.*
import java.text.SimpleDateFormat
import java.util.*

class AddActionHistoryActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val myCalendar = MutableLiveData<Calendar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_action_history)
        text_title.text = getString(R.string.input_sapi_add_action_history_title)

        myCalendar.value = Calendar.getInstance().also {
            it.time = Date(Long.MIN_VALUE)
        }
        subscribeUi()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        myCalendar.value = calendar
    }

    fun pickDate(view: View) {
        val calendar = if (checkDateNotMin(myCalendar.value!!)) myCalendar.value!! else Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    fun handleImageBackClick(view: View) {
        onBackPressed()
    }

    fun handleAddButtonClick(view: View) {
        if (checkFields()) {
            val actionHistory = ActionHistory(
                myCalendar.value!!.timeInMillis / 1000,
                text_action.text.toString().trim(),
                text_condition.text.toString().trim(),
                text_diagnostic.text.toString().trim(),
                text_drug.text.toString().trim()
            )
            val sendIntent = Intent()
            sendIntent.putExtra(ACTION_HISTORY, actionHistory)
            setResult(Activity.RESULT_OK, sendIntent)
            finish()
        }
    }


    private fun subscribeUi() {
        myCalendar.observe(this, androidx.lifecycle.Observer {
            if (checkDateNotMin(it)) {
                updateDateField(it)
            }
        })
    }

    private fun updateDateField(calendar: Calendar) {
        val myFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        text_date.text = sdf.format(calendar.time)
    }

    private fun checkFields(): Boolean {
        if (!checkDateNotMin(myCalendar.value!!)) {
            showAlertDialog(getString(R.string.input_sapi_error_date_field_must_be_filled))
            return false
        }

        val action = text_action.text.toString().trim()
        if (action.isEmpty()) {
            showAlertDialog(getString(R.string.input_sapi_error_action_field_must_be_filled))
            return false
        }

        val condition = text_condition.text.toString().trim()
        if (action.isEmpty()) {
            showAlertDialog(getString(R.string.input_sapi_error_cow_condition_must_be_filled))
            return false
        }

        //TODO : check("Asumsiku kolom diagnosa dan obat optional")

//        val diagnostic = text_diagnostic.text.toString().trim()
//        if (action.isEmpty()) {
//            showAlertDialog(getString(R.string.input_sapi_error_diagnostic_field_must_be_filled))
//            return false
//        }

        return true
    }

    private fun showAlertDialog(message: String) {
        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(this)
        }
        builder
            .setMessage(message)
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun checkDateNotMin(calendar: Calendar): Boolean {
        return calendar.time.compareTo(Date(Long.MIN_VALUE)) != 0
    }

    companion object {
        private const val ACTION_HISTORY = "ACTION_HISTORY"
    }
}

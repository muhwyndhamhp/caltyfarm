package com.caltyfarm.caltyfarm.ui

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.InputCowViewModel
import kotlinx.android.synthetic.main.activity_input_cow.*
import android.widget.TextView
import com.caltyfarm.caltyfarm.R.id.*
import java.util.*
import java.text.SimpleDateFormat
import kotlin.collections.HashMap


class InputCowActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var viewModel: InputCowViewModel
    private val hashMap: HashMap<Int, Int> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_cow)
        val factory = InjectorUtils.provideInputCowViewModelFactory()
        viewModel = ViewModelProviders.of(this, factory).get(InputCowViewModel::class.java)
        subscribeUi()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val index = hashMap[view!!.id]
        if (index in 0..4) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            when (index) {
                0 -> viewModel.birthCalendar.value = calendar
                1 -> viewModel.entryCalendar.value = calendar
                2 -> viewModel.outCalendar.value = calendar
                3 -> viewModel.pregnantCalendar.value = calendar
                4 -> viewModel.wormCalendar.value = calendar
            }
        }
    }

    fun pickDate(view: View) {
        val calendar = getCalendarByView(view)
        val datePicker = DatePickerDialog(
            this,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        hashMap[datePicker.datePicker.id] = getIndexByView(view)
        datePicker.show()
    }

    private fun subscribeUi() {
        viewModel.birthCalendar.observe(this, androidx.lifecycle.Observer {
            updateLabel(it, 0)
        })
        viewModel.entryCalendar.observe(this, androidx.lifecycle.Observer {
            updateLabel(it, 1)
        })
        viewModel.outCalendar.observe(this, androidx.lifecycle.Observer {
            updateLabel(it, 2)
        })
        viewModel.pregnantCalendar.observe(this, androidx.lifecycle.Observer {
            updateLabel(it, 3)
        })
        viewModel.wormCalendar.observe(this, androidx.lifecycle.Observer {
            updateLabel(it, 4)
        })
    }

    private fun updateLabel(calendar: Calendar, position: Int) {
        val myFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val textView = getTextViewByIndex(position)
        textView.text = sdf.format(calendar.time)
    }

    private fun getCalendarByView(view: View): Calendar {
        return when (view.id) {
            R.id.text_birth_date -> viewModel.birthCalendar.value!!
            R.id.text_entry_date -> viewModel.entryCalendar.value!!
            R.id.text_out_date -> viewModel.outCalendar.value!!
            R.id.text_pregnant_date -> viewModel.pregnantCalendar.value!!
            R.id.text_parasite_worm_drug -> viewModel.wormCalendar.value!!
            else -> Calendar.getInstance()
        }
    }

    private fun getIndexByView(view: View): Int {
        return when (view.id) {
            R.id.text_birth_date -> 0
            R.id.text_entry_date -> 1
            R.id.text_out_date -> 2
            R.id.text_pregnant_date -> 3
            R.id.text_parasite_worm_drug -> 4
            else -> 0
        }
    }

    private fun getTextViewByIndex(position: Int): TextView {
        return when (position) {
            0 -> text_birth_date
            1 -> text_entry_date
            2 -> text_out_date
            3 -> text_pregnant_date
            4 -> text_parasite_worm_drug
            else -> text_birth_date
        }
    }

    private fun checkDateNotZero(calendar: Calendar): Boolean {
        return calendar.time.compareTo(Date(0)) != 0
    }

    fun handleSubmitClick(view: View) {
    }

    fun handleImageBackClick(view: View) {
        onBackPressed()
    }
}
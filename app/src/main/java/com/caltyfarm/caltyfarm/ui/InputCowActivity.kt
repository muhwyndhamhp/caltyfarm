package com.caltyfarm.caltyfarm.ui

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.InputCowViewModel
import kotlinx.android.synthetic.main.activity_input_cow.*
import android.widget.TextView
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
        populateSpinner()
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

    fun handleSubmitClick(view: View) {
    }

    fun handleImageBackClick(view: View) {
        onBackPressed()
    }

    private fun subscribeUi() {
        viewModel.birthCalendar.observe(this, androidx.lifecycle.Observer {
            if (checkDateNotMin(it)) {
                updateLabel(it, 0)
            }
        })
        viewModel.entryCalendar.observe(this, androidx.lifecycle.Observer {
            if (checkDateNotMin(it)) {
                updateLabel(it, 1)
            }
        })
        viewModel.outCalendar.observe(this, androidx.lifecycle.Observer {
            if (checkDateNotMin(it)) {
                updateLabel(it, 2)
            }
        })
        viewModel.pregnantCalendar.observe(this, androidx.lifecycle.Observer {
            if (checkDateNotMin(it)) {
                updateLabel(it, 3)
            }
        })
    }

    private fun updateLabel(calendar: Calendar, position: Int) {
        val myFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val textView = getTextViewByIndex(position)
        textView.text = sdf.format(calendar.time)
    }

    private fun getCalendarByView(view: View): Calendar {
        val calendar = when (view.id) {
            R.id.text_birth_date -> viewModel.birthCalendar.value!!
            R.id.text_entry_date -> viewModel.entryCalendar.value!!
            R.id.text_out_date -> viewModel.outCalendar.value!!
            R.id.text_pregnant_date -> viewModel.pregnantCalendar.value!!
            else -> Calendar.getInstance()
        }
        return if (checkDateNotMin(calendar)) calendar else Calendar.getInstance()
    }

    private fun getIndexByView(view: View): Int {
        return when (view.id) {
            R.id.text_birth_date -> 0
            R.id.text_entry_date -> 1
            R.id.text_out_date -> 2
            R.id.text_pregnant_date -> 3
            else -> 0
        }
    }

    private fun getTextViewByIndex(position: Int): TextView {
        return when (position) {
            0 -> text_birth_date
            1 -> text_entry_date
            2 -> text_out_date
            3 -> text_pregnant_date
            else -> text_birth_date
        }
    }

    private fun checkDateNotMin(calendar: Calendar): Boolean {
        return calendar.time.compareTo(Date(Long.MIN_VALUE)) != 0
    }

    private fun populateSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.gender,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner_gender.adapter = adapter
        }
        spinner_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> femaleFieldToggle(false)
                    1 -> femaleFieldToggle(true)
                }
            }
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.breed,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner_breed.adapter = adapter
        }
    }

    private fun femaleFieldToggle(isFemale: Boolean) {
        if (isFemale) {
            label_pregnant_date.visibility = View.VISIBLE
            text_pregnant_date.visibility = View.VISIBLE
        } else {
            label_pregnant_date.visibility = View.GONE
            text_pregnant_date.visibility = View.GONE
        }
    }
}
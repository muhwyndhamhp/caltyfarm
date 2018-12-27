package com.caltyfarm.caltyfarm.ui.inputcow

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.viewmodel.InputCowViewModel
import kotlinx.android.synthetic.main.fragment_basic_input_cow.view.*
import java.text.SimpleDateFormat
import java.util.*
import android.os.Build
import androidx.appcompat.app.AlertDialog


class BasicInputFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: InputCowViewModel
    private val hashMap: HashMap<Int, Int> = hashMapOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_basic_input_cow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(InputCowViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        viewModel.pageTitle.value = getString(R.string.input_sapi_basic_input_title)
        addOnClickListener(view)
        populateSpinner(view)
        subscribeUi(view)
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
            context!!,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        hashMap[datePicker.datePicker.id] = getIndexByView(view)
        datePicker.show()
    }

    private fun subscribeUi(view: View) {
        viewModel.birthCalendar.observe(this, androidx.lifecycle.Observer {
            if (checkDateNotMin(it)) {
                updateLabel(view, it, 0)
            }
        })
        viewModel.entryCalendar.observe(this, androidx.lifecycle.Observer {
            if (checkDateNotMin(it)) {
                updateLabel(view, it, 1)
            }
        })
        viewModel.outCalendar.observe(this, androidx.lifecycle.Observer {
            if (checkDateNotMin(it)) {
                updateLabel(view, it, 2)
            }
        })
        viewModel.pregnantCalendar.observe(this, androidx.lifecycle.Observer {
            if (checkDateNotMin(it)) {
                updateLabel(view, it, 3)
            }
        })
    }

    private fun addOnClickListener(view: View) {
        view.text_birth_date.setOnClickListener { pickDate(view.text_birth_date) }
        view.text_entry_date.setOnClickListener { pickDate(view.text_entry_date) }
        view.text_out_date.setOnClickListener { pickDate(view.text_out_date) }
        view.text_pregnant_date.setOnClickListener { pickDate(view.text_pregnant_date) }
        view.button_next.setOnClickListener { handleNextButtonClick(view) }
    }

    private fun handleNextButtonClick(view: View) {
        if (isFieldsValid(view)) {
            viewModel.page.value = 1
        }
    }

    private fun isFieldsValid(view: View): Boolean {
        val id = view.text_id.text.toString().trim()
        val weight = view.text_weight.text.toString().trim()
        if (id.isEmpty()) {
            showAlertDialog(getString(R.string.input_sapi_error_id_must_not_empty))
            return false
        }
        try {
            id.toInt()
        } catch (e: Exception) {
            showAlertDialog(getString(R.string.input_sapi_error_id_must_number))
            return false
        }
        if (!checkDateNotMin(viewModel.birthCalendar.value!!)) {
            showAlertDialog(getString(R.string.input_sapi_error_birth_date_must_be_filled))
            return false
        }
        if (!checkDateNotMin(viewModel.entryCalendar.value!!)) {
            showAlertDialog(getString(R.string.input_sapi_error_entry_date_must_be_filled))
            return false
        }
//        if (!checkDateNotMin(viewModel.outCalendar.value!!)) {
//            showAlertDialog(getString(R.string.input_sapi_error_out_date_must_be_filled))
//            return false
//        }
        if (weight.isEmpty()) {
            showAlertDialog(getString(R.string.input_sapi_error_weight_must_not_be_empty))
            return false
        }
        try {
            weight.toDouble()
        } catch (e: Exception) {
            showAlertDialog(getString(R.string.input_sapi_error_weight_must_be_number))
            return false
        }
//        if (!checkDateNotMin(viewModel.pregnantCalendar.value!!) && view.spinner_gender.selectedItemPosition == 1) {
//            showAlertDialog(getString(R.string.input_sapi_error_pregnant_date_must_be_filled))
//            return false
//        }
        return true
    }

    private fun updateLabel(view: View, calendar: Calendar, position: Int) {
        val myFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val textView = getTextViewByIndex(view, position)
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

    private fun getTextViewByIndex(view: View, position: Int): TextView {
        return when (position) {
            0 -> view.text_birth_date
            1 -> view.text_entry_date
            2 -> view.text_out_date
            3 -> view.text_pregnant_date
            else -> view.text_birth_date
        }
    }

    private fun checkDateNotMin(calendar: Calendar): Boolean {
        return calendar.time.compareTo(Date(Long.MIN_VALUE)) != 0
    }

    private fun populateSpinner(fragmentView: View) {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.gender,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            fragmentView.spinner_gender.adapter = adapter
        }
        fragmentView.spinner_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> femaleFieldToggle(fragmentView, false)
                    1 -> femaleFieldToggle(fragmentView, true)
                }
            }
        }

        ArrayAdapter.createFromResource(
            context!!,
            R.array.breed,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            fragmentView.spinner_breed.adapter = adapter
        }
    }

    private fun femaleFieldToggle(view: View, isFemale: Boolean) {
        if (isFemale) {
            view.label_pregnant_date.visibility = View.VISIBLE
            view.text_pregnant_date.visibility = View.VISIBLE
        } else {
            view.label_pregnant_date.visibility = View.GONE
            view.text_pregnant_date.visibility = View.GONE
        }
    }

    private fun showAlertDialog(message: String) {
        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(context!!, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(context!!)
        }
        builder
            .setMessage(message)
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    companion object {
        fun newInstance() = BasicInputFragment()
    }
}
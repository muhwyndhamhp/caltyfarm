package com.caltyfarm.caltyfarm.ui.inputcow

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.customElements.CustomArrayAdapter
import com.caltyfarm.caltyfarm.data.model.Cow
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.InputCowViewModel
import kotlinx.android.synthetic.main.fragment_basic_input_cow.*
import kotlinx.android.synthetic.main.fragment_basic_input_cow.view.*
import java.text.SimpleDateFormat
import java.util.*


class BasicFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: InputCowViewModel

    private val hashMap: HashMap<Int, Int> = hashMapOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_basic_input_cow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this, InjectorUtils.provideInputCowViewModelFactory()).get(InputCowViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        populateSpinner(view)
        viewModel.cowData.observe(this, androidx.lifecycle.Observer {
            if(it != null)
                setData(it)
        })
    }

    private fun setData(it: Cow) {
        text_id.setText(it.id.toString())
        text_id.isEnabled = false
        val date = Date(it.birthDate)
        val df2 = SimpleDateFormat("dd/MM/yyyy")
        val dateText = df2.format(date)
        text_birth_date.text = dateText
        if(it.parentId != null) text_parent_id.apply {
            visibility = View.VISIBLE
            setText(it.parentId.toString())
        }

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

    private fun pickDate(view: View) {
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

    private fun populateSpinner(fragmentView: View) {
        CustomArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            Arrays.asList(*resources.getStringArray(R.array.gender))
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            fragmentView.spinner_gender.adapter = adapter
        }

        CustomArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            Arrays.asList(*resources.getStringArray(R.array.breed))
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            fragmentView.spinner_breed.adapter = adapter
        }

        CustomArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            Arrays.asList(*resources.getStringArray(R.array.age))
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            fragmentView.spinner_age.adapter = adapter
        }
    }

//    private fun loadFields(view: View) {
//        if (viewModel.cowId.value != null) {
//            view.text_id.setText(viewModel.cowId.value.toString())
//        }
//        view.spinner_age.setSelection(viewModel.ageIndex.value ?: 0)
//        if (viewModel.parentId.value != null) {
//            view.text_parent_id.setText(viewModel.parentId.value.toString())
//        }
//        view.spinner_breed.setSelection(viewModel.breedIndex.value ?: 0)
//        view.spinner_gender.setSelection(viewModel.genderIndex.value ?: 0)
//        if (viewModel.weight.value != null) {
//            view.text_weight.setText(viewModel.weight.value.toString())
//        }
//        view.checkbox_is_pregnant.isChecked = viewModel.isPregnant.value ?: false
//        if (viewModel.pregnantNumber.value != null) {
//            view.text_pregnant_number.setText(viewModel.pregnantNumber.value.toString())
//        }
//    }

    private fun childToggle(view: View, isChild: Boolean) {
        if (isChild) {
            view.label_parent_id.visibility = View.VISIBLE
            view.text_parent_id.visibility = View.VISIBLE
        } else {
            view.label_parent_id.visibility = View.GONE
            view.text_parent_id.visibility = View.GONE
        }

    }

    private fun possiblePregnantToggle(view: View, isPossiblePregnant: Boolean) {
        if (isPossiblePregnant && viewModel.isPregnant.value == true) {
            view.label_is_pregnant.visibility = View.VISIBLE
            view.checkbox_is_pregnant.visibility = View.VISIBLE
            view.label_pregnant_number.visibility = View.VISIBLE
            view.text_pregnant_number.visibility = View.VISIBLE
            view.label_pregnant_date.visibility = View.VISIBLE
            view.text_pregnant_date.visibility = View.VISIBLE
        } else if (isPossiblePregnant) {
            view.label_is_pregnant.visibility = View.VISIBLE
            view.checkbox_is_pregnant.visibility = View.VISIBLE
        } else {
            view.label_is_pregnant.visibility = View.GONE
            view.checkbox_is_pregnant.visibility = View.GONE
            view.label_pregnant_number.visibility = View.GONE
            view.text_pregnant_number.visibility = View.GONE
            view.label_pregnant_date.visibility = View.GONE
            view.text_pregnant_date.visibility = View.GONE
        }
    }

    private fun isPregnantToggle(view: View, isPregnant: Boolean) {
        if (isPregnant) {
            view.label_pregnant_number.visibility = View.VISIBLE
            view.text_pregnant_number.visibility = View.VISIBLE
            view.label_pregnant_date.visibility = View.VISIBLE
            view.text_pregnant_date.visibility = View.VISIBLE
        } else {
            view.label_pregnant_number.visibility = View.GONE
            view.text_pregnant_number.visibility = View.GONE
            view.label_pregnant_date.visibility = View.GONE
            view.text_pregnant_date.visibility = View.GONE
        }
    }

    private fun updateDateFields(view: View, calendar: Calendar, position: Int) {
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

    private fun checkDateNotMin(calendar: Calendar): Boolean {
        return calendar.time.compareTo(Date(Long.MIN_VALUE)) != 0
    }

    companion object {
        fun newInstance() = BasicInputFragment()
    }
}

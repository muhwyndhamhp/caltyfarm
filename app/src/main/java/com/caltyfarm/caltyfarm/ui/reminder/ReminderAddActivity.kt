package com.caltyfarm.caltyfarm.ui.reminder

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.caltyfarm.caltyfarm.R
import com.getbase.floatingactionbutton.FloatingActionButton
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.*


class ReminderAddActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    private var mToolbar: Toolbar? = null
    private var mTitleText: EditText? = null
    private var mDateText: TextView? = null
    private var mTimeText: TextView? = null
    private var mRepeatText: TextView? = null
    private var mRepeatNoText: TextView? = null
    private var mRepeatTypeText: TextView? = null
    private var mFAB1: FloatingActionButton? = null
    private var mFAB2: FloatingActionButton? = null
    private var mCalendar: Calendar? = null
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var mDay: Int = 0
    private var mRepeatTime: Long = 0
    private var mTitle: String? = null
    private var mTime: String? = null
    private var mDate: String? = null
    private var mRepeat: String? = null
    private var mRepeatNo: String? = null
    private var mRepeatType: String? = null
    private var mActive: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        // Initialize Views
        mToolbar = findViewById(R.id.toolbar)
        mTitleText = findViewById(R.id.reminder_title)
        mDateText = findViewById(R.id.set_date)
        mTimeText = findViewById(R.id.set_time)
        mRepeatText = findViewById(R.id.set_repeat)
        mRepeatNoText = findViewById(R.id.set_repeat_no)
        mRepeatTypeText = findViewById(R.id.set_repeat_type)
        mFAB1 = findViewById(R.id.starred1)
        mFAB2 = findViewById(R.id.starred2)

        // Setup Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar!!.setTitle(R.string.title_activity_add_reminder)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        // Initialize default values
        mActive = "true"
        mRepeat = "true"
        mRepeatNo = Integer.toString(1)
        mRepeatType = "Hour"

        mCalendar = Calendar.getInstance()
        mHour = mCalendar!!.get(Calendar.HOUR_OF_DAY)
        mMinute = mCalendar!!.get(Calendar.MINUTE)
        mYear = mCalendar!!.get(Calendar.YEAR)
        mMonth = mCalendar!!.get(Calendar.MONTH) + 1
        mDay = mCalendar!!.get(Calendar.DATE)

        mDate = mDay.toString() + "/" + mMonth + "/" + mYear
        mTime = mHour.toString() + ":" + mMinute

        // Setup Reminder Title EditText
        mTitleText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mTitle = s.toString().trim { it <= ' ' }
                mTitleText!!.error = null
            }

            override fun afterTextChanged(s: Editable) {}
        })

        // Setup TextViews using reminder values
        mDateText!!.text = mDate
        mTimeText!!.text = mTime
        mRepeatNoText!!.text = mRepeatNo
        mRepeatTypeText!!.text = mRepeatType
        mRepeatText!!.text = """Every $mRepeatNo $mRepeatType(s)"""

        // To save state on device rotation
        if (savedInstanceState != null) {
            val savedTitle = savedInstanceState.getString(KEY_TITLE)
            mTitleText!!.setText(savedTitle)
            mTitle = savedTitle

            val savedTime = savedInstanceState.getString(KEY_TIME)
            mTimeText!!.text = savedTime
            mTime = savedTime

            val savedDate = savedInstanceState.getString(KEY_DATE)
            mDateText!!.text = savedDate
            mDate = savedDate

            val saveRepeat = savedInstanceState.getString(KEY_REPEAT)
            mRepeatText!!.text = saveRepeat
            mRepeat = saveRepeat

            val savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO)
            mRepeatNoText!!.text = savedRepeatNo
            mRepeatNo = savedRepeatNo

            val savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE)
            mRepeatTypeText!!.text = savedRepeatType
            mRepeatType = savedRepeatType

            mActive = savedInstanceState.getString(KEY_ACTIVE)
        }

        // Setup up active buttons
        if (mActive == "false") {
            mFAB1!!.visibility = View.VISIBLE
            mFAB2!!.visibility = View.GONE

        } else if (mActive == "true") {
            mFAB1!!.visibility = View.GONE
            mFAB2!!.visibility = View.VISIBLE
        }
    }

    // To save state on device rotation
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putCharSequence(KEY_TITLE, mTitleText!!.text)
        outState.putCharSequence(KEY_TIME, mTimeText!!.text)
        outState.putCharSequence(KEY_DATE, mDateText!!.text)
        outState.putCharSequence(KEY_REPEAT, mRepeatText!!.text)
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText!!.text)
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText!!.text)
        outState.putCharSequence(KEY_ACTIVE, mActive)
    }

    // On clicking Time picker
    fun setTime(v: View) {
        val now = Calendar.getInstance()
        val tpd = TimePickerDialog.newInstance(
            this,
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            false
        )
        tpd.isThemeDark = false
        tpd.show(fragmentManager, "Timepickerdialog")
    }

    // On clicking Date picker
    fun setDate(v: View) {
        val now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
            this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show(fragmentManager, "Datepickerdialog")
    }

    // Obtain time from time picker
    override fun onTimeSet(view: RadialPickerLayout, hourOfDay: Int, minute: Int) {
        mHour = hourOfDay
        mMinute = minute
        mTime = if (minute < 10) {
            hourOfDay.toString() + ":" + "0" + minute
        } else {
            hourOfDay.toString() + ":" + minute
        }
        mTimeText!!.text = mTime
    }

    // Obtain date from date picker
    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var monthOfYears = monthOfYear
        monthOfYears++
        mDay = dayOfMonth
        mMonth = monthOfYears
        mYear = year
        mDate = dayOfMonth.toString() + "/" + monthOfYears + "/" + year
        mDateText!!.text = mDate
    }

    // On clicking the active button
    fun selectFab1(v: View) {
        mFAB1 = findViewById(R.id.starred1)
        mFAB1!!.visibility = View.GONE
        mFAB2 = findViewById(R.id.starred2)
        mFAB2!!.visibility = View.VISIBLE
        mActive = "true"
    }

    // On clicking the inactive button
    fun selectFab2(v: View) {
        mFAB2 = findViewById(R.id.starred2)
        mFAB2!!.visibility = View.GONE
        mFAB1 = findViewById(R.id.starred1)
        mFAB1!!.visibility = View.VISIBLE
        mActive = "false"
    }

    // On clicking the repeat switch
    fun onSwitchRepeat(view: View) {
        val on = (view as Switch).isChecked
        if (on) {
            mRepeat = "true"
            mRepeatText!!.text = "Every $mRepeatNo $mRepeatType(s)"
        } else {
            mRepeat = "false"
            mRepeatText!!.setText(R.string.repeat_off)
        }
    }

    // On clicking repeat type button
    fun selectRepeatType(v: View) {
        val items = arrayOfNulls<String>(5)

        items[0] = "Minute"
        items[1] = "Hour"
        items[2] = "Day"
        items[3] = "Week"
        items[4] = "Month"

        // Create List Dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Type")
        builder.setItems(items) { _, item ->
            mRepeatType = items[item]
            mRepeatTypeText!!.text = mRepeatType
            mRepeatText!!.text = "Every $mRepeatNo $mRepeatType(s)"
        }
        val alert = builder.create()
        alert.show()
    }

    // On clicking repeat interval button
    fun setRepeatNo(v: View) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Enter Number")

        // Create EditText box to input repeat number
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        alert.setView(input)
        alert.setPositiveButton(
            "Ok"
        ) { _, _ ->
            if (input.text.toString().isEmpty()) {
                mRepeatNo = Integer.toString(1)
                mRepeatNoText!!.text = mRepeatNo
                mRepeatText!!.text = "Every $mRepeatNo $mRepeatType(s)"
            } else {
                mRepeatNo = input.text.toString().trim { it <= ' ' }
                mRepeatNoText!!.text = mRepeatNo
                mRepeatText!!.text = "Every $mRepeatNo $mRepeatType(s)"
            }
        }
        alert.setNegativeButton("Cancel") { _, _ ->
            // do nothing
        }
        alert.show()
    }

    // On clicking the save button
    private fun saveReminder() {
        val rb = ReminderDatabase(this)

        // Creating Reminder
        val id = rb.addReminder(Reminder(mTitle, mDate, mTime, mRepeat, mRepeatNo, mRepeatType, mActive))

        // Set up calender for creating the notification
        mCalendar!!.set(Calendar.MONTH, --mMonth)
        mCalendar!!.set(Calendar.YEAR, mYear)
        mCalendar!!.set(Calendar.DAY_OF_MONTH, mDay)
        mCalendar!!.set(Calendar.HOUR_OF_DAY, mHour)
        mCalendar!!.set(Calendar.MINUTE, mMinute)
        mCalendar!!.set(Calendar.SECOND, 0)

        // Check repeat type
        when (mRepeatType) {
            "Minute" -> mRepeatTime = Integer.parseInt(mRepeatNo!!) * milMinute
            "Hour" -> mRepeatTime = Integer.parseInt(mRepeatNo!!) * milHour
            "Day" -> mRepeatTime = Integer.parseInt(mRepeatNo!!) * milDay
            "Week" -> mRepeatTime = Integer.parseInt(mRepeatNo!!) * milWeek
            "Month" -> mRepeatTime = Integer.parseInt(mRepeatNo!!) * milMonth
        }

        // Create a new notification

        // Create toast to confirm new reminder

        // Create a new notification
        if (mActive == "true") {
            if (mRepeat == "true") {
                AlarmReceiver().setRepeatAlarm(applicationContext, mCalendar, id, mRepeatTime)
            } else if (mRepeat == "false") {
                AlarmReceiver().setAlarm(applicationContext, mCalendar, id)
            }
        }

        // Create toast to confirm new reminder
        Toast.makeText(
            applicationContext, "Saved",
            Toast.LENGTH_SHORT
        ).show()

        onBackPressed()
    }

    // Creating the menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_reminder, menu)
        return true
    }

    // On clicking menu buttons
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            // On clicking the back arrow
            // Discard any changes
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            // On clicking save reminder button
            // Update reminder
            R.id.save_reminder -> {
                mTitleText!!.setText(mTitle)

                if (mTitleText!!.text.toString().isEmpty())
                    mTitleText!!.error = "Reminder Title cannot be blank!"
                else {
                    saveReminder()
                }
                return true
            }

            // On clicking discard reminder button
            // Discard any changes
            R.id.discard_reminder -> {
                Toast.makeText(
                    applicationContext, "Discarded",
                    Toast.LENGTH_SHORT
                ).show()

                onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {

        // Values for orientation change
        private const val KEY_TITLE = "title_key"
        private const val KEY_TIME = "time_key"
        private const val KEY_DATE = "date_key"
        private const val KEY_REPEAT = "repeat_key"
        private const val KEY_REPEAT_NO = "repeat_no_key"
        private const val KEY_REPEAT_TYPE = "repeat_type_key"
        private const val KEY_ACTIVE = "active_key"

        // Constant values in milliseconds
        private const val milMinute = 60000L
        private const val milHour = 3600000L
        private const val milDay = 86400000L
        private const val milWeek = 604800000L
        private const val milMonth = 2592000000L
    }
}
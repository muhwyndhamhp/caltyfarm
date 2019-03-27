package com.caltyfarm.caltyfarm.ui.inputcow

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.InputCowViewModel
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.caltyfarm.caltyfarm.data.model.ActionHistory
import kotlinx.android.synthetic.main.activity_input_cow.*
import kotlin.collections.HashMap


class InputCowActivity : AppCompatActivity() {
    private lateinit var viewModel: InputCowViewModel
    private val hashMap: HashMap<Int, Int> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_cow)
        val factory = InjectorUtils.provideInputCowViewModelFactory()
        viewModel = ViewModelProviders.of(this, factory).get(InputCowViewModel::class.java)
        subscribeUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_ACTION_HISTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.addActionHistory(data!!.getSerializableExtra(ACTION_HISTORY) as ActionHistory)
        }
    }

    fun handleImageBackClick(view: View) {
        onBackPressed()
    }

    private fun subscribeUi() {
        viewModel.page.observe(this, Observer {
            replaceFragment(selectFragment(it))
        })
        viewModel.pageTitle.observe(this, Observer {
            text_title.text = it
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.replace(R.id.frame_layout, fragment)
        ft.commit()
    }

    private fun selectFragment(position: Int): Fragment {
        return when (position) {
            0 -> BasicInputFragment.newInstance()
            1 -> InputActionHistoryFragment.newInstance()
            else -> BasicInputFragment.newInstance()
        }
    }

    companion object {
        private const val ADD_ACTION_HISTORY_REQUEST_CODE: Int = 212
        private const val ACTION_HISTORY = "ACTION_HISTORY"
    }
}
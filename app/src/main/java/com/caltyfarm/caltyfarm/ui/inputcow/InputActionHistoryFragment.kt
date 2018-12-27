package com.caltyfarm.caltyfarm.ui.inputcow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.ActionHistory
import com.caltyfarm.caltyfarm.utils.TableUtils
import com.caltyfarm.caltyfarm.viewmodel.InputCowViewModel
import kotlinx.android.synthetic.main.fragment_input_action_history_fragment.*
import java.util.*

class InputActionHistoryFragment : Fragment() {

    lateinit var viewModel: InputCowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_input_action_history_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(InputCowViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        viewModel.pageTitle.value = getString(R.string.input_sapi_action_history_input_title)
        subscribeUi(view)

        val dummyData = listOf(
            ActionHistory(Date(), "1aaaaaaaaaaaaaaaaaaaaaaaaaa", "zsdfgsdzfsdfgasdfgsadfasdfasdf2", "3", "4"),
            ActionHistory(Date(), "6", "7", "8", "9"),
            ActionHistory(Date(), "10", "11", "12", "13")
        )
        web_view.loadDataWithBaseURL(
            "file:///android_asset/",
            TableUtils.listOfActionHistoryToTableHtml(dummyData),
            "text/html",
            "UTF-8",
            null
        )
    }

    private fun subscribeUi(view: View) {

    }

    companion object {
        fun newInstance() = InputActionHistoryFragment()
    }

}
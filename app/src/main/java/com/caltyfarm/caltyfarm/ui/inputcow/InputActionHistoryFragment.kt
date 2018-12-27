package com.caltyfarm.caltyfarm.ui.inputcow

import android.content.Intent
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
import kotlinx.android.synthetic.main.fragment_input_action_history_fragment.view.*

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
        setButtonOnClickListener(view)
        subscribeUi(view)

    }

    private fun setButtonOnClickListener(view: View) {
        view.button_add.setOnClickListener { openAddActionHistoryActivity() }
        view.button_save.setOnClickListener { saveInput(view) }
    }

    private fun openAddActionHistoryActivity() {
        activity!!.startActivityForResult(
            Intent(activity, AddActionHistoryActivity::class.java), ADD_ACTION_HISTORY_REQUEST_CODE
        )
    }

    private fun saveInput(view: View) {
    }

    private fun subscribeUi(view: View) {
        viewModel.actionHistoryList.observe(this, androidx.lifecycle.Observer {
            showActionHistoryTable(view, it)
        })
    }

    private fun showActionHistoryTable(view: View, actionHistoryList: List<ActionHistory>) {
        if (actionHistoryList.isEmpty()) {
            view.web_view.loadData("Tidak ada data", "text/html", "UTF-8")
        } else {
            view.web_view.loadData(
                TableUtils.listOfActionHistoryToTableHtml(actionHistoryList), "text/html", "UTF-8"
            )
        }
    }

    companion object {
        private const val ADD_ACTION_HISTORY_REQUEST_CODE: Int = 212
        fun newInstance() = InputActionHistoryFragment()
    }

}
package com.caltyfarm.caltyfarm.ui.cowdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Cow
import com.caltyfarm.caltyfarm.ui.inputcow.ActionHistoryFragment
import com.caltyfarm.caltyfarm.ui.inputcow.BasicFragment
import com.caltyfarm.caltyfarm.utils.COW_DATA_KEY
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.InputCowViewModel

class CowDetailActivity : AppCompatActivity() {

    lateinit var viewModel: InputCowViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cow_detail)
        val factory = InjectorUtils.provideInputCowViewModelFactory(intent.getSerializableExtra(COW_DATA_KEY) as Cow)
        viewModel = ViewModelProviders.of(this, factory).get(InputCowViewModel::class.java)
        initFrameLayout()
    }

    private fun initFrameLayout() {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.replace(R.id.frame_basic, BasicFragment.newInstance())
        ft.replace(R.id.frame_detail, ActionHistoryFragment.newInstance())
        ft.commit()
    }
}

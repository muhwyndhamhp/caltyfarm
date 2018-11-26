package com.caltyfarm.caltyfarm.ui

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.ui.fragments.ArticleFragment
import com.caltyfarm.caltyfarm.ui.fragments.HomeFragment
import com.caltyfarm.caltyfarm.ui.fragments.MoreFragment
import com.caltyfarm.caltyfarm.ui.fragments.ProfileFragment
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.indeterminateProgressDialog
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigation(bottomNavigationView)

        val factory = InjectorUtils.provideMainViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        viewModel.fragmentPosition.observe(this, Observer { replaceFragment(selectFragment(it)) })
        viewModel.setFragmentPosition(0)
    }

    private fun replaceFragment(selectFragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame_layout, selectFragment)
        ft.commit()
    }

    private fun selectFragment(it: Int): Fragment {
        return when(it){
            0 -> HomeFragment.newInstance()
            1 -> ProfileFragment.newInstance()
            2 -> ArticleFragment.newInstance()
            3 -> MoreFragment.newInstance()
            else -> HomeFragment.newInstance()
        }

    }

    private fun setupBottomNavigation(bottomNavigationView: AHBottomNavigation) {
        val context = this
        val ahBottomNavigationAdapter = AHBottomNavigationAdapter(this, R.menu.menu_main)
        ahBottomNavigationAdapter.setupWithBottomNavigation(bottomNavigationView)
        bottomNavigationView.apply {
            //            titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
            accentColor = ContextCompat.getColor(context, R.color.colorAccent)
            inactiveColor = ContextCompat.getColor(context, R.color.defaultUnselected)
            defaultBackgroundColor = ContextCompat.getColor(context, R.color.white)
            setOnTabSelectedListener { position, _ -> viewModel.setFragmentPosition(position)
                true
            }
        }
    }

    fun showLoading(body: String, title: String) {
        progressDialog = indeterminateProgressDialog(message = body, title = title)
        progressDialog.setProgressStyle(R.style.MyAlertDialogStyle)
        progressDialog.show()
    }

    fun dismissLoading(){
        Timer().schedule(2000){
            if(progressDialog.isShowing) progressDialog.dismiss()
        }

    }
}

package com.caltyfarm.caltyfarm.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.PHONE_NUMBER_CODE
import com.caltyfarm.caltyfarm.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_auth.*
import org.jetbrains.anko.toast

class AuthActivity : AppCompatActivity() {

    lateinit var viewModel: AuthViewModel

    private val REQUEST_SMS_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val factory = InjectorUtils.provideAuthViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)

        et_phone.setOnKeyListener { v, keyCode, event ->

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (setupPermission()) {
                    navigateToVerif()
                    true
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_SMS),
                        REQUEST_SMS_CODE
                    )
                    false
                }
            } else false
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_SMS_CODE -> {
                if(grantResults.isNotEmpty()){
                    if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                        toast("Permintaan membaca SMS ditolak!")
                    } else {
                        navigateToVerif()
                    }
                }
            }
        }
    }

    private fun navigateToVerif() {
        val intent = Intent(this, VerificationActivity::class.java)
        intent.putExtra(PHONE_NUMBER_CODE, getPhoneNumber())
        startActivity(intent)
    }

    private fun setupPermission(): Boolean {

        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)

        return permission == PackageManager.PERMISSION_GRANTED
    }

    private fun getPhoneNumber() =
        ccp.selectedCountryCodeWithPlus + et_phone.text.trim().replaceFirst("^0+(?!$)".toRegex(), "")

}

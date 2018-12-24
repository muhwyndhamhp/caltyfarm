package com.caltyfarm.caltyfarm

import android.app.Application
import com.qiscus.sdk.Qiscus

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        Qiscus.init(this, resources.getString(R.string.APP_ID))
    }
}
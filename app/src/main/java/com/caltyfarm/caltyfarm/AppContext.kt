package com.caltyfarm.caltyfarm

import android.app.Application
import com.qiscus.sdk.Qiscus
import org.greenrobot.eventbus.EventBus

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        Qiscus.init(this, resources.getString(R.string.APP_ID))
        Qiscus.getChatConfig().apply {
            statusBarColor = R.color.colorPrimaryDark
            appBarColor = R.color.colorPrimary
            rightBubbleColor = R.color.colorPrimary
            rightBubbleTextColor = R.color.white
            rightBubbleTimeColor = R.color.white
            rightLinkTextColor = R.color.defaultUnselected
            isEnablePushNotification = true
            isEnableFcmPushNotification = true
            isOnlyEnablePushNotificationOutsideChatRoom = true
        }

    }

}
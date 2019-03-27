package com.caltyfarm.caltyfarm.service

import com.google.firebase.messaging.RemoteMessage
import com.qiscus.sdk.service.QiscusFirebaseService

class MyFirebaseMessagingService : QiscusFirebaseService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

    }
}
package ru.lopav.kzn.fb

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp

class FbApp: Application() {

    lateinit var logger: AppEventsLogger

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS)
        logger = AppEventsLogger.newLogger(this)
    }
}

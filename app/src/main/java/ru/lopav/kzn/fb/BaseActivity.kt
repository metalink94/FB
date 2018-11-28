package ru.lopav.kzn.fb

import android.support.v7.app.AppCompatActivity
import com.facebook.appevents.AppEventsLogger

open class BaseActivity: AppCompatActivity() {

    fun getLogger(): AppEventsLogger =
        (application as FbApp).logger
}

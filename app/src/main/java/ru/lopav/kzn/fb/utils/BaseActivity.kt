package ru.lopav.kzn.fb.utils

import android.support.v7.app.AppCompatActivity
import com.facebook.appevents.AppEventsLogger
import ru.lopav.kzn.fb.FbApp

open class BaseActivity: AppCompatActivity() {

    fun getLogger(): AppEventsLogger =
        (application as FbApp).logger
}

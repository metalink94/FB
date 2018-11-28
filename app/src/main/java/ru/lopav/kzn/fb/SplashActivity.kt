package ru.lopav.kzn.fb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getLogger().logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP)
        checkDatabase()
    }

    private fun checkDatabase() {
        val database = FirebaseDatabase.getInstance()
        database.reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val web = p0.child("web").value as Boolean
                    val url = p0.child("url").value as String?
                    router(web, url)
                }
                Log.d("DataBase", "get database ${p0.value}")
            }

            override fun onCancelled(p0: DatabaseError) {
                router()
            }
        })
    }

    private fun router(isWeb: Boolean = false, url: String? = null) {
        if (isWeb && !url.isNullOrEmpty()) {
            showWeb(url)
        } else {
            showMainScreen()
        }
    }

    private fun showMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showWeb(url: String) {
        startActivity(WebViewActivity.getInstance(this, url))
    }
}

package ru.lopav.kzn.fb.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.airbnb.deeplinkdispatch.DeepLink
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.lopav.kzn.fb.BuildConfig
import ru.lopav.kzn.fb.R
import ru.lopav.kzn.fb.utils.BaseActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplashActivity : BaseActivity() {

    companion object {
        // это будет именем файла настроек
        val APP_PREFERENCES = "mysettings"
        val APP_PREFERENCES_Link = "Deep_Link"
    }

    private var webUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        getHashKey()
        checkDatabase(getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE))
    }

    private fun getHashKey() {
        try {
            val info = packageManager.getPackageInfo(
                BuildConfig.APP_PACKAGE,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }

    }

    private fun checkDatabase(sharedPreferences: SharedPreferences) {
        val database = FirebaseDatabase.getInstance()
        database.reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val link = p0.child("link").value as Boolean
                    val web = p0.child("web").value as Boolean
                    webUrl = p0.child("url").value as String?
                    checkDeepLink(sharedPreferences, web, webUrl, link)
                }
                Log.d("DataBase", "get database ${p0.value}")
            }

            override fun onCancelled(p0: DatabaseError) {
                checkDeepLink(sharedPreferences)
            }
        })
    }

    private fun checkDeepLink(
        sharedPreferences: SharedPreferences,
        isWeb: Boolean = false,
        url: String? = null,
        link: Boolean = false
    ) {
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false) ||
            sharedPreferences.getBoolean(APP_PREFERENCES_Link, false) ||
            link
        ) {
            setPreference(sharedPreferences, true)
            router(isWeb, url)
        } else {
            router()
        }
    }

    private fun setPreference(sharedPreferences: SharedPreferences, state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(APP_PREFERENCES_Link, state)
        editor.apply()
    }

    private fun router(isWeb: Boolean = false, url: String? = null) {
        if (isWeb && !url.isNullOrEmpty()) {
            showWeb(url)
        } else {
            showMainScreen()
        }
    }

    private fun showMainScreen() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    private fun showWeb(url: String?) {
        startActivity(WebViewActivity.getInstance(this, url))
        finish()
    }
}

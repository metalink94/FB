package ru.lopav.kzn.fb.activities

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.menu_activity.*
import ru.lopav.kzn.fb.utils.BaseActivity
import ru.lopav.kzn.fb.R

class MenuActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)
        space.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        lotery.setOnClickListener {
            startActivity(Intent(this, LoteryActivity::class.java))
            finish()
        }
    }
}

package ru.lopav.kzn.fb.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.lotery_activity.*
import ru.lopav.kzn.fb.R
import ru.lopav.kzn.fb.utils.BaseActivity
import ru.lopav.kzn.fb.utils.FlipView
import kotlin.concurrent.fixedRateTimer

class LoteryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lotery_activity)
        btn.setOnClickListener { startGame() }
    }

    private fun startGame() {
/*
        for (i in 0..7) {
            Handler().postDelayed(
                {
                    flip(flipView2)
                }, 300
            )
        }

        for (i in 0..9) {
            Handler().postDelayed(
                {
                    flip(flipView3)
                }, 300
            )
        }*/
    }

    private fun flip(view: FlipView) {
        Log.d("Lotery", "Flip() ${view.isFrontSide}")
        if (view.isFrontSide) {
            Log.d("Lotery", "Flip() inFrontSize")
            view.flipToState(FlipView.FlipState.BACK_SIDE)
        } else {
            Log.d("Lotery", "Flip() inBackSize")
            view.flipToState(FlipView.FlipState.FRONT_SIDE)
        }
    }
}

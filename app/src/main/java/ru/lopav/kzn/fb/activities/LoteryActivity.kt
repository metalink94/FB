package ru.lopav.kzn.fb.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.lotery_activity.*
import ru.lopav.kzn.fb.R
import ru.lopav.kzn.fb.dialogs.CongratulationDialog
import ru.lopav.kzn.fb.dialogs.DialogListener
import ru.lopav.kzn.fb.utils.BaseActivity
import ru.lopav.kzn.fb.utils.FlipView
import kotlin.random.Random

class LoteryActivity : BaseActivity(), DialogListener {

    private var isWin: Boolean = false

    override fun onButtonClick() {
        flip(flipView)
        flip(flipView2)
        flip(flipView3)
        isWin = false
        randomizeGame()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lotery_activity)
        flipView.flipTheView(false)
        flipView2.flipTheView(false)
        flipView3.flipTheView(false)
        randomizeGame()
        btn.setOnClickListener { startGame() }
    }

    private fun randomizeGame() {
        val r = Random.nextInt(85) + 1
        when {
            r % 3 == 0 -> {winnerPos()}
                r % 7 == 0 -> {winnerPos()}
            else -> {losePos(r)}

        }
    }

    private fun losePos(r: Int) {
        val newR = Random.nextInt(r)
        when {
            newR % 2 == 0 -> {
                back1.setImageResource(R.drawable.ic_cup)
                back2.setImageResource(R.drawable.ic_cross)
                back3.setImageResource(R.drawable.ic_moon)
            }
            newR % 3 == 0 -> {
                back1.setImageResource(R.drawable.ic_cross)
                back2.setImageResource(R.drawable.ic_cup)
                back3.setImageResource(R.drawable.ic_shuttle)
            }
            newR % 5 == 0 -> {
                back1.setImageResource(R.drawable.ic_cup)
                back2.setImageResource(R.drawable.ic_moon)
                back3.setImageResource(R.drawable.ic_cup)
            }
            newR % 7 == 0 -> {
                back1.setImageResource(R.drawable.ic_cup)
                back2.setImageResource(R.drawable.ic_shuttle)
                back3.setImageResource(R.drawable.ic_meteorite)
            }
        }
    }

    private fun winnerPos() {
        back1.setImageResource(R.drawable.ic_cup)
        back2.setImageResource(R.drawable.ic_cup)
        back3.setImageResource(R.drawable.ic_cup)
        isWin = true
    }

    private fun startGame() {
        if (checkViews()) {
            showResults()
            return
        }
        flip(flipView)
        Handler().postDelayed({
            flip(flipView2)
        }, 300)
        Handler().postDelayed({
            flip(flipView3)
        }, 600)
        Handler().postDelayed({
            if (checkViews()) {
                showResults()
            }
        }, 950)
    }

    private fun checkViews(): Boolean {
        return flipView.isFrontSide &&
                flipView2.isFrontSide &&
                flipView3.isFrontSide
    }

    private fun showResults() {
        val dialog = CongratulationDialog.getInstance(isWin)
        dialog.listener = this
        supportFragmentManager.beginTransaction().add(dialog, "").commitAllowingStateLoss()
    }

    private fun flip(view: FlipView) {
        if (view.isFrontSide) {
            view.flipToState(FlipView.FlipState.BACK_SIDE)
        } else {
            view.flipToState(FlipView.FlipState.FRONT_SIDE)
        }
    }
}

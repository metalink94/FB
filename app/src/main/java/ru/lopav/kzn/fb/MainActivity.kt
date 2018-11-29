package ru.lopav.kzn.fb

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), DialogListener {

    private var pos = 0
    private var loseAttempt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startGame()

    }

    private fun startGame() {
        loseAttempt = 0
        reDrawCircles()
        setWinnerPosition()
        setGridLayout()
    }

    private fun setWinnerPosition() {
        pos = Random.nextInt(9) + 1
        Log.d("FB", "get random number $pos")
    }

    private fun setGridLayout() {
        gridLayout.removeAllViews()
        addViews()
    }

    private fun addViews() {
        for (i in 1..9) {
            val flipView = ViewFlipper(this)
            flipView.setInAnimation(this, android.R.anim.fade_in)
            flipView.setOutAnimation(this, android.R.anim.fade_out)
            flipView.addView(addFlipperContent(i, flipView))
            flipView.addView(addFlipperIconContent(i))
            flipView.setPadding(8, 8, 8, 8)
            gridLayout.addView(flipView)
        }
    }

    private fun addFlipperIconContent(i: Int): View {
        val relative2 = RelativeLayout(this)
        relative2.layoutParams =
                ViewGroup.LayoutParams(BUTTON_H_W, BUTTON_H_W)
        relative2.setBackgroundResource(R.drawable.rectangle_grey)
        relative2.addView(addIcon(i))
        return relative2
    }

    private fun addIcon(position: Int): View {
        val icon = ImageView(this)
        val layoutParams = RelativeLayout.LayoutParams(ICON_H_W, ICON_H_W)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        icon.layoutParams = layoutParams
        if (position == pos) {
            icon.setImageResource(R.drawable.ic_cup)
        } else {
            icon.setImageResource(R.drawable.ic_cross)
        }
        return icon
    }

    private fun addFlipperContent(i: Int, flipView: ViewFlipper): View {
        val relative1 = RelativeLayout(this)
        relative1.layoutParams = ViewGroup.LayoutParams(BUTTON_H_W, BUTTON_H_W)
        relative1.setBackgroundResource(R.drawable.rectangle_grey)
        relative1.addView(addTextView(i))
        relative1.setOnClickListener { onCardClick(i, flipView) }
        return relative1
    }

    private fun addTextView(i: Int): View {
        val textView = TextView(this)
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        textView.layoutParams = layoutParams
        textView.text = i.toString()
        textView.textSize = TEXT_SIZE
        return textView
    }

    private fun onCardClick(position: Int, flipView: ViewFlipper) {
        if (position == pos) {
            flipView.showNext()
            showDialog(true)
        } else {
            checkAttempt(flipView)
        }
    }

    private fun showDialog(state: Boolean) {
        Handler().postDelayed(
            {
                showWinnerDialog(state)
            }, 100
        )
    }

    private fun checkAttempt(flipView: ViewFlipper) {
        loseAttempt += 1
        reDrawCircles()
        if (loseAttempt < 3) {
            flipView.showNext()
        } else {
            flipView.showNext()
            showDialog(false)
        }
    }

    private fun reDrawCircles() {
        when (loseAttempt) {
            0 -> drawWhite()
            1 -> circle1.setBackgroundResource(R.drawable.circle_with_corner)
            2 -> circle2.setBackgroundResource(R.drawable.circle_with_corner)
            3 -> circle3.setBackgroundResource(R.drawable.circle_with_corner)
        }
    }

    private fun drawWhite() {
        circle1.setBackgroundResource(R.drawable.circle)
        circle2.setBackgroundResource(R.drawable.circle)
        circle3.setBackgroundResource(R.drawable.circle)
    }

    private fun showWinnerDialog(isWin: Boolean) {
        val fragment = CongratulationDialog.getInstance(isWin)
        fragment.listener = this
        supportFragmentManager.beginTransaction().add(fragment, "").commitAllowingStateLoss()
    }

    override fun onButtonClick() {
        startGame()
    }

    companion object {
        private const val BUTTON_H_W = 180
        private const val ICON_H_W = 160
        private const val TEXT_SIZE = 24f
    }
}

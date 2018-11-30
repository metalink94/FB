package ru.lopav.kzn.fb

import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewFlipper
import kotlinx.android.synthetic.main.activity_main.*
import ru.lopav.kzn.fb.dialogs.CongratulationDialog
import ru.lopav.kzn.fb.dialogs.DialogListener
import ru.lopav.kzn.fb.dialogs.HelloDialog
import ru.lopav.kzn.fb.dialogs.MoneyDialog
import kotlin.random.Random


class MainActivity : BaseActivity(), DialogListener {

    private var pos = 0
    private var loseAttempt = 0
    private var height = 0
    private var width = 0
    private var goldMoney = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getScreenSettings()
        showHelloAlert()
        startGame()
    }

    private fun showHelloAlert() {
        val fragment = HelloDialog()
        supportFragmentManager.beginTransaction().add(fragment, "tag").commitAllowingStateLoss()
    }

    private fun getScreenSettings() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        width = metrics.widthPixels / 4
        height = metrics.heightPixels / 3
        if (3 * width > metrics.heightPixels / 3 * 2) {
            height = metrics.heightPixels / 3 * 2
            width = height / 4
        }
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
                ViewGroup.LayoutParams(width, width)
        relative2.setBackgroundResource(R.drawable.rectangle_grey)
        relative2.addView(addIcon(i))
        return relative2
    }

    private fun addIcon(position: Int): View {
        val icon = ImageView(this)
        val layoutParams = RelativeLayout.LayoutParams(width / 3 * 2, width / 3 * 2)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        icon.layoutParams = layoutParams
        if (position == pos) {
            icon.setImageResource(R.drawable.ic_cup)
        } else {
            icon.setImageResource(R.drawable.ic_cross)
        }
        return icon
    }

    private fun addIconPreview(position: Int): View {
        val icon = ImageView(this)
        val layoutParams = RelativeLayout.LayoutParams(width / 3 * 2, width / 3 * 2)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        icon.layoutParams = layoutParams
        icon.setImageResource(getImageResource())
        return icon
    }

    private fun getImageResource(): Int {
        val r = Random.nextInt(20)
        return when {
            r % 3 == 0 -> R.drawable.ic_meteorite
            r % 4 == 0 -> R.drawable.ic_planet
            r % 5 == 0 -> R.drawable.ic_spaceship
            r % 7 == 0 -> R.drawable.ic_shuttle
            else -> R.drawable.ic_moon
        }
    }

    private fun addFlipperContent(i: Int, flipView: ViewFlipper): View {
        val relative1 = RelativeLayout(this)
        relative1.layoutParams = ViewGroup.LayoutParams(width, width)
        relative1.setBackgroundResource(R.drawable.rectangle_grey)
        relative1.addView(addIconPreview(i))
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
                changeGold(state)
            }, 100
        )
    }

    private fun changeGold(state: Boolean) {
        if (state) {
            goldMoney += 500
        } else {
            goldMoney -= 100
        }
        gold.text = goldMoney.toString()
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
        checkMoney()
    }

    private fun checkMoney() {
        if (goldMoney < 0) {
            val fragment = MoneyDialog()
            supportFragmentManager.beginTransaction().add(fragment, "money").commitAllowingStateLoss()
            goldMoney = 900
            gold.text = goldMoney.toString()
        }
    }

    companion object {
        private const val TEXT_SIZE = 24f
    }
}

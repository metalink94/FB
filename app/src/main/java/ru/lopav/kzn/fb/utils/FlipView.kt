package ru.lopav.kzn.fb.utils

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import ru.lopav.kzn.fb.R

class FlipView : FrameLayout {

    private var setRightOut: AnimatorSet = AnimatorSet()
    private var setLeftIn: AnimatorSet = AnimatorSet()
    private var cardFrontLayout: View? = null
    private var cardBackLayout: View? = null

    /**
     * Whether view is set to flip on touch or not.
     *
     * @return true or false
     */
    /**
     * Set whether view should be flipped on touch or not!
     *
     * @param flipOnTouch value (true or false)
     */
    var isFlipOnTouch = true
    private var flipDuration: Int = 0
    /**
     * Returns whether flip is enabled or not!
     *
     * @return true or false
     */
    /**
     * Enable / Disable flip view.
     *
     * @param flipEnabled true or false
     */
    var isFlipEnabled: Boolean = false

    /**
     * Returns which flip state is currently on of the flip view.
     *
     * @return current state of flip view
     */
    var currentFlipState = FlipState.FRONT_SIDE
        private set

    var onCardFlipListener: OnCardFlipListener? = null

    /**
     * Returns true if the front side of flip view is visible.
     *
     * @return true if the front side of flip view is visible.
     */
    val isFrontSide: Boolean
        get() = currentFlipState == FlipState.FRONT_SIDE

    /**
     * Returns true if the back side of flip view is visible.
     *
     * @return true if the back side of flip view is visible.
     */
    val isBackSide: Boolean
        get() = currentFlipState == FlipState.BACK_SIDE

    enum class FlipState {
        FRONT_SIDE,
        BACK_SIDE
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        // Setting Default Values
        isFlipOnTouch = true
        flipDuration = DEFAULT_FLIP_DURATION
        isFlipEnabled = true

        loadAnimations()

    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (childCount > 2) {
            throw IllegalStateException("FlipView can host only two direct children!")
        }

        findViews()
        changeCameraDistance()
    }

    override fun addView(v: View, pos: Int, params: ViewGroup.LayoutParams) {
        if (childCount == 2) {
            throw IllegalStateException("FlipView can host only two direct children!")
        }

        super.addView(v, pos, params)

        findViews()
        changeCameraDistance()
    }

    override fun removeView(v: View) {
        super.removeView(v)

        findViews()
    }

    override fun removeAllViewsInLayout() {
        super.removeAllViewsInLayout()

        // Reset the state
        currentFlipState = FlipState.FRONT_SIDE

        findViews()
    }

    private fun findViews() {
        // Invalidation since we use this also on removeView
        cardBackLayout = null
        cardFrontLayout = null

        val childCount = childCount
        if (childCount < 1) {
            return
        }

        if (childCount < 2) {
            // Only invalidate flip state if we have a single child
            currentFlipState = FlipState.FRONT_SIDE

            cardFrontLayout = getChildAt(0)
        } else if (childCount == 2) {
            cardFrontLayout = getChildAt(1)
            cardBackLayout = getChildAt(0)
        }

        if (!isFlipOnTouch) {
            cardFrontLayout?.visibility = View.VISIBLE

            if (cardBackLayout != null) {
                cardBackLayout?.visibility = View.GONE
            }
        }
    }

    private fun loadAnimations() {
        setRightOut = AnimatorInflater.loadAnimator(context, R.animator.animator_flip_out) as AnimatorSet
        setLeftIn = AnimatorInflater.loadAnimator(context, R.animator.animator_flip_in) as AnimatorSet
        setRightOut.interpolator = AccelerateDecelerateInterpolator()
        setLeftIn.interpolator = AccelerateDecelerateInterpolator()

        setRightOut.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {

                if (onCardFlipListener != null) {
                    onCardFlipListener?.onFlip(currentFlipState)
                }

                if (currentFlipState == FlipState.FRONT_SIDE) {
                    cardBackLayout?.visibility = View.GONE
                    cardFrontLayout?.visibility = View.VISIBLE
                } else {
                    cardBackLayout?.visibility = View.VISIBLE
                    cardFrontLayout?.visibility = View.GONE
                }
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
        setFlipDuration(flipDuration)
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance

        if (cardFrontLayout != null) {
            cardFrontLayout?.cameraDistance = scale
        }
        if (cardBackLayout != null) {
            cardBackLayout?.cameraDistance = scale
        }
    }

    /**
     * Play the animation of flipping and flip the view for one side!
     */
    fun flipTheView() {
        if (!isFlipEnabled || childCount < 2)
            return

        if (setRightOut.isRunning || setLeftIn.isRunning)
            return

        cardBackLayout?.visibility = View.VISIBLE
        cardFrontLayout?.visibility = View.VISIBLE

        if (currentFlipState == FlipState.FRONT_SIDE) {
            // From front to back
            setRightOut.setTarget(cardFrontLayout)
            setLeftIn.setTarget(cardBackLayout)
            setRightOut.start()
            setLeftIn.start()
            currentFlipState = FlipState.BACK_SIDE
        } else {
            // from back to front
            setRightOut.setTarget(cardBackLayout)
            setLeftIn.setTarget(cardFrontLayout)
            setRightOut.start()
            setLeftIn.start()
            currentFlipState = FlipState.FRONT_SIDE
        }
    }

    fun flipToState(state: FlipState) {
        if (this.currentFlipState == state) {
            return
        }
        flipTheView()
    }

    /**
     * Flip the view for one side with or without animation.
     *
     * @param withAnimation true means flip view with animation otherwise without animation.
     */
    fun flipTheView(withAnimation: Boolean) {
        if (childCount < 2)
            return

        if (!withAnimation) {
            setLeftIn.duration = 0
            setRightOut.duration = 0
            val oldFlipEnabled = isFlipEnabled
            isFlipEnabled = true

            flipTheView()

            setLeftIn.duration = flipDuration.toLong()
            setRightOut.duration = flipDuration.toLong()
            isFlipEnabled = oldFlipEnabled
        } else {
            flipTheView()
        }

    }

    /**
     * Returns duration of flip in milliseconds!
     *
     * @return duration in milliseconds
     */
    fun getFlipDuration(): Int {
        return flipDuration
    }

    /**
     * Sets the flip duration (in milliseconds)
     *
     * @param flipDuration duration in milliseconds
     */
    fun setFlipDuration(flipDuration: Int) {
        this.flipDuration = flipDuration
        if (setRightOut.childAnimations.size < 2 || setLeftIn.childAnimations.size < 2) {
            return
        }

        //setRightOut.setDuration(flipDuration);
        setRightOut.childAnimations[0].duration = flipDuration.toLong()
        setRightOut.childAnimations[1].startDelay = (flipDuration / 2).toLong()

        //setLeftIn.setDuration(flipDuration);
        setLeftIn.childAnimations[1].duration = flipDuration.toLong()
        setLeftIn.childAnimations[2].startDelay = (flipDuration / 2).toLong()
    }

    interface OnCardFlipListener {
        fun onFlip(newState: FlipState)
    }

    companion object {

        val TAG = FlipView::class.java.simpleName

        val DEFAULT_FLIP_DURATION = 300
    }
}

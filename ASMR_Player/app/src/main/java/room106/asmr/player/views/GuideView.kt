package room106.asmr.player.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import room106.asmr.player.R
import room106.asmr.player.SharedPreferencesManager
import room106.asmr.player.activities.MainActivity


class GuideView: LinearLayout {

    constructor(context: Context?) : super(context) {
        initializeView(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initializeView(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initializeView(context)
    }

    private var isGuideSuccess = false

    private fun initializeView(context: Context?) {
        View.inflate(context, R.layout.guide_layout, this )

        if (context != null) {
            isGuideSuccess = SharedPreferencesManager(context).isGuideSuccess()

            visibility = if (isGuideSuccess) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    fun hideGuidePanel() {
        if (visibility == View.VISIBLE && !isGuideSuccess) {
            val displayMetrics = DisplayMetrics()
            (context as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            val w = displayMetrics.widthPixels

            animate().translationX(-w.toFloat()).withEndAction {
                val valueAnimator = ValueAnimator.ofInt(measuredHeight, 0)

                valueAnimator.addUpdateListener {
                    val animatedValue = valueAnimator.animatedValue as Int
                    val params = layoutParams
                    params.height = animatedValue
                    layoutParams = params
                    layoutParams.height = animatedValue
                }

                valueAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = View.GONE

                        // Update SharedPreferences
                        SharedPreferencesManager(context).setGuide(true)
                    }
                })

                valueAnimator.duration = 300
                valueAnimator.startDelay = 200
                valueAnimator.start()
            }.duration = 400
        }
    }
}
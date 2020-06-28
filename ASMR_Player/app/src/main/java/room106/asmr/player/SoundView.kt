package room106.asmr.player

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView


class SoundView(context: Context?, title: String) : LinearLayout(context) {

    // Views
    private var mIconView: ImageButton
    private var mTitleView: TextView
    private var mControlPanel: LinearLayout

    private var controlPanelIsVisible = false
    private var controlPanelMeasuredHeight: Int

    init {
        View.inflate(context, R.layout.sound_layout, this)

        mIconView = findViewById(R.id.soundIcon)
        mTitleView = findViewById(R.id.soundTitle)
        mControlPanel = findViewById(R.id.controlPanel)
        mControlPanel.visibility = View.INVISIBLE

        mControlPanel.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        controlPanelMeasuredHeight = mControlPanel.measuredHeight

        mTitleView.text = title

        mIconView.setOnClickListener {
            controlPanelIsVisible = !controlPanelIsVisible


            if (controlPanelIsVisible) {
                // Show panel
                mControlPanel.visibility = View.VISIBLE

                val valueAnimator =
                    ValueAnimator.ofInt(0, controlPanelMeasuredHeight)

                valueAnimator.addUpdateListener {
                    val animatedValue = valueAnimator.animatedValue as Int
                    val layoutParams = mControlPanel.layoutParams
                    layoutParams.height = animatedValue
                    mControlPanel.layoutParams = layoutParams
                }

                valueAnimator.duration = 300
                valueAnimator.start()

            } else {
                // Hide panel
                val valueAnimator =
                    ValueAnimator.ofInt(controlPanelMeasuredHeight, 0)

                valueAnimator.addUpdateListener {
                    val animatedValue = valueAnimator.animatedValue as Int
                    val layoutParams = mControlPanel.layoutParams
                    layoutParams.height = animatedValue
                    mControlPanel.layoutParams = layoutParams
                }

                valueAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        mControlPanel.visibility = View.INVISIBLE
                    }
                })

                valueAnimator.duration = 300
                valueAnimator.start()
            }
        }
    }

}
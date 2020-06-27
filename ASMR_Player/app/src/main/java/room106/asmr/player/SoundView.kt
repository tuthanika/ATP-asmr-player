package room106.asmr.player

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

class SoundView(context: Context?, title: String) : LinearLayout(context) {

    // Views
    private lateinit var mIconView: ImageButton
    private lateinit var mTitleView: TextView
    private lateinit var mControlPanel: LinearLayout

    private var controlPanelIsVisible = false

    init {
        View.inflate(context, R.layout.sound_layout, this)

        mIconView = findViewById(R.id.soundIcon)
        mTitleView = findViewById(R.id.soundTitle)
        mControlPanel = findViewById(R.id.controlPanel)
        mControlPanel.visibility = View.GONE

        mTitleView.text = title

        mIconView.setOnClickListener {
            controlPanelIsVisible = !controlPanelIsVisible

            if (controlPanelIsVisible) {
                // Show panel
                mControlPanel.visibility = View.VISIBLE
            } else {
                // Hide panel
                mControlPanel.visibility = View.GONE
            }
        }
    }

}
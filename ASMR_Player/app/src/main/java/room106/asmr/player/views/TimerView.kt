package room106.asmr.player.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import room106.asmr.player.R
import room106.asmr.player.activities.TimerActivity

class TimerView: LinearLayout {

    //region Constructors
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    // Views
    private lateinit var mTimerIconImageView: ImageView
    private lateinit var mTimerTitleTextView: TextView

    // States
    private var _isTimerSelected = false
    private var timerID = -1

    constructor(context: Context?, timerTitle: String, id: Int) : super(context) {
        View.inflate(context,
            R.layout.timer_item_layout, this)

        // Connect views
        mTimerIconImageView = findViewById(R.id.timerIcon)
        mTimerTitleTextView = findViewById(R.id.timerTitle)

        mTimerTitleTextView.text = timerTitle
        timerID = id

        setOnClickListener {
            (context as TimerActivity).selectTimer(timerID)
        }
    }

    var isTimerSelected: Boolean
        get() {
            return _isTimerSelected
        }
        set (newValue) {
            _isTimerSelected = newValue

            // Update view
           if (_isTimerSelected) {
               // Selected timer
               mTimerIconImageView.setImageResource(R.drawable.ic_timer_white)
               mTimerIconImageView.setBackgroundResource(R.drawable.selected_timer_icon_background)
           } else {
               // Not selected timer
               mTimerIconImageView.setImageResource(R.drawable.ic_timer)
               mTimerIconImageView.setBackgroundResource(R.drawable.sound_icon_background)
           }
        }

}
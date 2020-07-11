package room106.asmr.player.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import room106.asmr.player.R
import room106.asmr.player.TimerEventReceiver
import room106.asmr.player.views.TimerView

class TimerActivity : AppCompatActivity() {

    // Views
    private lateinit var mHeader: TextView
    private lateinit var mTimersList: LinearLayout
    private lateinit var mCancelTimerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        mHeader = findViewById(R.id.timerHeaderTextView)
        mTimersList = findViewById(R.id.timersList)
        mCancelTimerButton = findViewById(R.id.cancelTimerButton)
        mCancelTimerButton.visibility = View.GONE

        val timer15m = TimerView(this, "15 minutes", 0, 15)
        val timer30m = TimerView(this, "30 minutes", 1, 30)
        val timer45m = TimerView(this, "45 minutes", 2, 45)
        val timer1h = TimerView(this, "1 hour", 3, 60)
        val timer2h = TimerView(this, "2 hour", 4, 120)
        val timer3h = TimerView(this, "3 hour", 5, 180)

        mTimersList.addView(timer15m)
        mTimersList.addView(timer30m)
        mTimersList.addView(timer45m)
        mTimersList.addView(timer1h)
        mTimersList.addView(timer2h)
        mTimersList.addView(timer3h)
    }

    fun selectTimer(timerID: Int, timerMinutes: Int) {

        // Update view
        for (i in 0 until mTimersList.childCount) {
            (mTimersList[i] as TimerView).isTimerSelected = timerID == i
        }

        TimerEventReceiver().setUpTimer(this, timerMinutes)

        // Show "Cancel timer" button
        mCancelTimerButton.visibility = View.VISIBLE
    }

    fun onClickCancelTimer(v: View) {

        // Update timer views
        for (i in 0 until mTimersList.childCount) {
            (mTimersList[i] as TimerView).isTimerSelected = false
        }

        TimerEventReceiver().cancelTimer(this)

        // Hide "Cancel timer" button
        mCancelTimerButton.visibility = View.GONE
    }

    fun onClickBack(v: View) {
        finish()
        overridePendingTransition(
            R.anim.freeze,
            R.anim.slide_out_left
        )
    }
}
package room106.asmr.player.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.get
import room106.asmr.player.R
import room106.asmr.player.views.TimerView

class TimerActivity : AppCompatActivity() {

    // Views
    private lateinit var mTimersList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        mTimersList = findViewById(R.id.timersList)

        val timerOff = TimerView(this, "Off", 0)
            .apply { isTimerSelected = true }
        val timer15m = TimerView(this, "15 minutes", 1)
        val timer30m = TimerView(this, "30 minutes", 2)
        val timer45m = TimerView(this, "45 minutes", 3)
        val timer1h = TimerView(this, "1 hour", 4)
        val timer2h = TimerView(this, "2 hour", 5)
        val timer3h = TimerView(this, "3 hour", 6)

        mTimersList.addView(timerOff)
        mTimersList.addView(timer15m)
        mTimersList.addView(timer30m)
        mTimersList.addView(timer45m)
        mTimersList.addView(timer1h)
        mTimersList.addView(timer2h)
        mTimersList.addView(timer3h)
    }

    fun selectTimer(timerID: Int) {
        for (i in 0 until mTimersList.childCount) {
            (mTimersList[i] as TimerView).isTimerSelected = timerID == i
        }
    }

    fun onClickBack(v: View) {
        finish()
        overridePendingTransition(
            R.anim.freeze,
            R.anim.slide_out_left
        )
    }
}
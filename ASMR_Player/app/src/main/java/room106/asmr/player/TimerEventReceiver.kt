package room106.asmr.player

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*


class TimerEventReceiver: BroadcastReceiver()  {

    fun setUpTimer(context: Context, minutes: Int) {
        Log.d(TAG, "Set up timer for: $minutes")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimerEventReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, getTriggerDelay(Date(), minutes), alarmIntent)
    }

    fun cancelTimer(context: Context) {
        Log.d(TAG, "Cancel timer!")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimerEventReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(alarmIntent)
    }

    private fun getTriggerDelay(now: Date, minutes: Int): Long {
        Log.d(TAG, "setup alarm with delay $minutes")
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = now
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.timeInMillis
    }

    override fun onReceive(context: Context?, p1: Intent?) {
        Log.d(TAG, "FINISH APP!!")

        val intent = Intent(context, CloseActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context?.startActivity(intent)
    }

    companion object {
        const val TAG = "Timer"
    }
}
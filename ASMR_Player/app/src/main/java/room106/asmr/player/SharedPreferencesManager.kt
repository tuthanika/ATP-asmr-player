package room106.asmr.player

import android.content.Context

class SharedPreferencesManager(context: Context) {

    private val mSharedPreferences =
        context.getSharedPreferences("roo106.asmr.player", Context.MODE_PRIVATE)

    private val guideKey = "room106.asmr.player.guide"

    fun setGuide(value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(guideKey, value)
        editor.apply()
    }

    fun isGuideSuccess(): Boolean {
        return mSharedPreferences.getBoolean(guideKey, false)
    }
}
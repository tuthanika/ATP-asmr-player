package room106.asmr.player

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    // Views
    private lateinit var listNatureSound: LinearLayout
    private lateinit var listAsrmSound: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listNatureSound = findViewById(R.id.natureSoundList)
        listAsrmSound = findViewById(R.id.asmrSoundList)

        val s1 = SoundView(this, "Wind", true, R.raw.thunderstorm_30s)
        val s2 = SoundView(this, "Rain", true, R.raw.thunderstorm_30s)
        val s3 = SoundView(this, "Fire", false, R.raw.thunderstorm_30s)
        val s4 = SoundView(this, "Sea", false, R.raw.thunderstorm_30s)

        val s5 = SoundView(this, "Wind", true, R.raw.thunderstorm_30s)
        val s6 = SoundView(this, "Rain", false, R.raw.thunderstorm_30s)
        val s7 = SoundView(this, "Fire", false, R.raw.thunderstorm_30s)
        val s8 = SoundView(this, "Sea", false, R.raw.thunderstorm_30s)

        listNatureSound.addView(s1)
        listNatureSound.addView(s2)
        listNatureSound.addView(s3)
        listNatureSound.addView(s4)

        listAsrmSound.addView(s5)
        listAsrmSound.addView(s6)
        listAsrmSound.addView(s7)
        listAsrmSound.addView(s8)

    }

    fun onClickFavorites(v: View) {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.freeze)
    }

    fun onClickSleepTimer(v: View) {
        val intent = Intent(this, TimerActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.freeze)
    }

    fun onClickBuyProVersion(v: View) {
        val intent = Intent(this, ProVersionActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.freeze)
    }
}
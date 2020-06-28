package room106.asmr.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val s1 = SoundView(this, "Wind", true)
        val s2 = SoundView(this, "Rain", true)
        val s3 = SoundView(this, "Fire", false)
        val s4 = SoundView(this, "Sea", false)

        val s5 = SoundView(this, "Wind", true)
        val s6 = SoundView(this, "Rain", false)
        val s7 = SoundView(this, "Fire", false)
        val s8 = SoundView(this, "Sea", false)

        listNatureSound.addView(s1)
        listNatureSound.addView(s2)
        listNatureSound.addView(s3)
        listNatureSound.addView(s4)

        listAsrmSound.addView(s5)
        listAsrmSound.addView(s6)
        listAsrmSound.addView(s7)
        listAsrmSound.addView(s8)


    }
}
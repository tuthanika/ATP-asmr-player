package room106.asmr.player.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import room106.asmr.player.ASMR
import room106.asmr.player.R
import room106.asmr.player.views.GuideView
import room106.asmr.player.views.SoundView

class MainActivity : AppCompatActivity() {

    // Views
    private lateinit var naturalSoundsList: LinearLayout
    private lateinit var processSoundsList: LinearLayout
    private lateinit var asmrSoundsList: LinearLayout
    private lateinit var mGuideView: GuideView
    // Bổ sung: Nút reload mixes
    private var buttonReloadMixes: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        naturalSoundsList = findViewById(R.id.naturalSoundsList)
        processSoundsList = findViewById(R.id.processSoundsList)
        asmrSoundsList = findViewById(R.id.asmrSoundsList)
        mGuideView = findViewById(R.id.guideView)
        buttonReloadMixes = findViewById(R.id.buttonReloadMixes)

        ASMR.player.initializeMediaPlayers(this)

        //region Create SoundViews
        val s1 = SoundView(this, ASMR.Sound.FIREPLACE, R.drawable.ic_fire)
        val s2 = SoundView(this, ASMR.Sound.RAIN, R.drawable.ic_rain)
        val s3 = SoundView(this, ASMR.Sound.SEA, R.drawable.ic_sea)
        val s4 = SoundView(this, ASMR.Sound.TRAIN, R.drawable.ic_train)
        val s5 = SoundView(this, ASMR.Sound.WATER_DROPS, R.drawable.ic_water)
        val s6 = SoundView(this, ASMR.Sound.WIND, R.drawable.ic_wind)

        val s7 = SoundView(this, ASMR.Sound.BLOWING, R.drawable.ic_wind)
        val s8 = SoundView(this, ASMR.Sound.CUTTING, R.drawable.ic_cut)
        val s9 = SoundView(this, ASMR.Sound.EATING, R.drawable.ic_eat)
        val s10 = SoundView(this, ASMR.Sound.HEAD_SCRATCHING, R.drawable.ic_hand_5)
        val s11 = SoundView(this, ASMR.Sound.TYPING, R.drawable.ic_keyboard)

        val s12 = SoundView(this, ASMR.Sound.BOTTLE, R.drawable.ic_bottle)
        val s13 = SoundView(this, ASMR.Sound.GLOVES, R.drawable.ic_hand_7)
        val s14 = SoundView(this, ASMR.Sound.HANDS_1, R.drawable.ic_hand_1)
        val s15 = SoundView(this, ASMR.Sound.HANDS_2, R.drawable.ic_hand_1)
        val s16 = SoundView(this, ASMR.Sound.HANDS_3, R.drawable.ic_hand_1)
        val s17 = SoundView(this, ASMR.Sound.MASSAGE, R.drawable.ic_hand_5)
        val s18 = SoundView(this, ASMR.Sound.MOUTH, R.drawable.ic_mouth)
        val s19 = SoundView(this, ASMR.Sound.PACKAGING, R.drawable.ic_paper)
        val s20 = SoundView(this, ASMR.Sound.TAPPING_1, R.drawable.ic_hand_4)
        val s21 = SoundView(this, ASMR.Sound.TAPPING_2, R.drawable.ic_hand_4)
        val s22 = SoundView(this, ASMR.Sound.TAPPING_3, R.drawable.ic_hand_4)
        val s23 = SoundView(this, ASMR.Sound.WATER, R.drawable.ic_water)

        naturalSoundsList.addView(s1)
        naturalSoundsList.addView(s2)
        naturalSoundsList.addView(s3)
        naturalSoundsList.addView(s4)
        naturalSoundsList.addView(s5)
        naturalSoundsList.addView(s6)

        processSoundsList.addView(s7)
        processSoundsList.addView(s8)
        processSoundsList.addView(s9)
        processSoundsList.addView(s10)
        processSoundsList.addView(s11)

        asmrSoundsList.addView(s12)
        asmrSoundsList.addView(s13)
        asmrSoundsList.addView(s14)
        asmrSoundsList.addView(s15)
        asmrSoundsList.addView(s16)
        asmrSoundsList.addView(s17)
        asmrSoundsList.addView(s18)
        asmrSoundsList.addView(s19)
        asmrSoundsList.addView(s20)
        asmrSoundsList.addView(s21)
        asmrSoundsList.addView(s22)
        asmrSoundsList.addView(s23)
        //endregion

        // Bổ sung: reload lại danh sách mixes (nếu cần)
        buttonReloadMixes?.setOnClickListener {
            ASMR.player.reloadMixesFromPrefs(this)
        }
    }

    override fun onResume() {
        super.onResume()
        updateSoundView()
    }

    private fun updateSoundView() {

        val lists = ArrayList<LinearLayout>().apply {
            add(naturalSoundsList)
            add(processSoundsList)
            add(asmrSoundsList)
        }

        for (soundsList in lists) {
            for (i in 0 until soundsList.childCount) {
                val soundView = soundsList[i] as SoundView
                soundView.updateSliderValues()
                soundView.updateIcon()
            }
        }
    }

    fun onClickFavorites(v: View) {
        val intent = Intent(this, FavoritesActivity::class.java)

        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.freeze
        )
    }

    fun onClickSleepTimer(v: View) {
        val intent = Intent(this, TimerActivity::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.freeze
        )
    }

    fun onClickBuyProVersion(v: View) {
        val intent = Intent(this, ProVersionActivity::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_bottom,
            R.anim.freeze
        )
    }

    fun hideGuidePanel() {
        mGuideView.hideGuidePanel()
    }
}
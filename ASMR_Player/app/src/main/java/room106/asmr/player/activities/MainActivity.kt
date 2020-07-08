package room106.asmr.player.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.gson.Gson
import room106.asmr.player.CloseActivity
import room106.asmr.player.R
import room106.asmr.player.models.Mix
import room106.asmr.player.views.GuideView
import room106.asmr.player.views.SoundView


class MainActivity : AppCompatActivity() {

    // Views
    private lateinit var naturalSoundsList: LinearLayout
    private lateinit var processSoundsList: LinearLayout
    private lateinit var asmrSoundsList: LinearLayout
    private lateinit var mGuideView: GuideView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        naturalSoundsList = findViewById(R.id.naturalSoundsList)
        processSoundsList = findViewById(R.id.processSoundsList)
        asmrSoundsList = findViewById(R.id.asmrSoundsList)
        mGuideView = findViewById(R.id.guideView)

        //region Create SoundViews
        val s1 = SoundView(this, "Fireplace", true, R.raw.fireplace, R.drawable.ic_fire)
        val s2 = SoundView(this, "Rain", true, R.raw.rain, R.drawable.ic_rain)
        val s3 = SoundView(this, "Sea", true, R.raw.sea, R.drawable.ic_sea)
        val s4 = SoundView(this, "Train", true, R.raw.train, R.drawable.ic_train)
        val s5 = SoundView(this, "Water Drops", true, R.raw.water_drops, R.drawable.ic_water)
        val s6 = SoundView(this, "Wind", true, R.raw.wind, R.drawable.ic_wind)

        val s7 = SoundView(this, "Blowing", true, R.raw.blowing, R.drawable.ic_wind)
        val s8 = SoundView(this, "Cutting", true, R.raw.cutting, R.drawable.ic_cut)
        val s9 = SoundView(this, "Eating", true, R.raw.eating, R.drawable.ic_eat)
        val s10 = SoundView(this, "Hea Scratching", true, R.raw.head_scratching, R.drawable.ic_hand_5)
        val s11 = SoundView(this, "Typing", true, R.raw.typing, R.drawable.ic_keyboard)

        val s12 = SoundView(this, "Bottle", true, R.raw.bottle, R.drawable.ic_bottle)
        val s13 = SoundView(this, "Gloves", true, R.raw.gloves, R.drawable.ic_hand_7)
        val s14 = SoundView(this, "Hands #1", true, R.raw.hands_1, R.drawable.ic_hand_1)
        val s15 = SoundView(this, "Hands #2", true, R.raw.hands_2, R.drawable.ic_hand_1)
        val s16 = SoundView(this, "Hands #3", true, R.raw.hands_3, R.drawable.ic_hand_1)
        val s17 = SoundView(this, "Massage", true, R.raw.massage, R.drawable.ic_hand_5)
        val s18 = SoundView(this, "Mouth", true, R.raw.mouth, R.drawable.ic_mouth)
        val s19 = SoundView(this, "Packaging", true, R.raw.packaging, R.drawable.ic_paper)
        val s20 = SoundView(this, "Tapping #1", true, R.raw.tapping_1, R.drawable.ic_hand_4)
        val s21 = SoundView(this, "Tapping #2", true, R.raw.tapping_2, R.drawable.ic_hand_4)
        val s22 = SoundView(this, "Tapping #3", true, R.raw.tapping_3, R.drawable.ic_hand_4)
        val s23 = SoundView(this, "Water", true, R.raw.water, R.drawable.ic_water)


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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val mixJSON = data?.getStringExtra("playMix")
            Log.d("JSON", "playMix is ALLIVE. JSON: \n $mixJSON")

            val mixToPlay = Gson().fromJson(mixJSON, Mix::class.java)

            val lists = ArrayList<LinearLayout>().apply {
                add(naturalSoundsList)
                add(processSoundsList)
                add(asmrSoundsList)
            }

            for (soundsList in lists) {
                for (i in 0 until soundsList.childCount) {
                    val soundView = soundsList[i] as SoundView

                    var isFound = false

                    for (sound in mixToPlay.getSoundsList()) {

                        if (soundView.isTitleEqual(sound.title)) {
                            soundView.setSoundObject(sound)
                            soundView.play()
                            isFound = true
                            break
                        }
                    }
                    if (!isFound) {
                        soundView.pause()
                    }
                }
            }
        }
    }

    fun onClickFavorites(v: View) {
        val intent = Intent(this, FavoritesActivity::class.java)
        val mixJSON = analyzeCurrentMix()
        intent.putExtra("currentMixJSON", mixJSON)
        startActivityForResult(intent, 1)
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

    private fun analyzeCurrentMix(): String {

        val lists = ArrayList<LinearLayout>().apply {
            add(naturalSoundsList)
            add(processSoundsList)
            add(asmrSoundsList)
        }

        var currentMix = Mix()

        for (soundsList in lists) {
            for (i in 0 until soundsList.childCount) {
                val soundView = soundsList[i] as SoundView

                if (soundView.isPlaying()) {
                    val sound = soundView.getSoundObject()
                    if (sound != null) {
                        currentMix.add(sound)
                    }
                }
            }
        }

        val mixJSON = Gson().toJson(currentMix)
        Log.d("JSON", "CurrentMix: $mixJSON")
        return mixJSON
    }

    fun hideGuidePanel() {
        mGuideView.hideGuidePanel()
    }

    override fun onDestroy() {
        super.onDestroy()

        val lists = ArrayList<LinearLayout>().apply {
            add(naturalSoundsList)
            add(processSoundsList)
            add(asmrSoundsList)
        }

        for (soundsList in lists) {
            for (i in 0 until soundsList.childCount) {
                val soundView = soundsList[i] as SoundView
                soundView.pause()
            }
        }
    }
}
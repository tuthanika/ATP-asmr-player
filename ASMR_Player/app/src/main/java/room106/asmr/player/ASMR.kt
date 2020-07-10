package room106.asmr.player

import android.content.Context
import com.google.gson.Gson
import room106.asmr.player.models.Mix
import room106.asmr.player.models.MixesList
import room106.asmr.player.models.SoundProperties


class ASMR private constructor() {


    enum class Sound(val title: String, val resID: Int, val isFree: Boolean) {
        FIREPLACE("Fire", R.raw.fireplace, true),
        RAIN("Rain", R.raw.rain, true),
        SEA("Sea", R.raw.sea, true),
        TRAIN("Train", R.raw.train, true),
        WATER_DROPS("Water Drops", R.raw.water_drops, true),
        WIND("Wind", R.raw.wind, true),

        BLOWING("Blowing", R.raw.blowing, true),
        CUTTING("Cutting", R.raw.cutting, true),
        EATING("Eating", R.raw.eating, true),
        HEAD_SCRATCHING("Head Scratching", R.raw.head_scratching, true),
        TYPING("Typing", R.raw.typing, true),

        BOTTLE("Bottle", R.raw.bottle, true),
        GLOVES("Gloves", R.raw.gloves, true),
        HANDS_1("Hands #1", R.raw.hands_1, true),
        HANDS_2("Hands #2", R.raw.hands_2, true),
        HANDS_3("Hands #3", R.raw.hands_3, true),
        MASSAGE("Massage", R.raw.massage, true),
        MOUTH("Mouth", R.raw.mouth, true),
        PACKAGING("Packaging", R.raw.packaging, true),
        TAPPING_1("Tapping #1", R.raw.tapping_1, true),
        TAPPING_2("Tapping #2", R.raw.tapping_2, true),
        TAPPING_3("Tapping #3", R.raw.tapping_3, true),
        WATER("Water", R.raw.water, true)
    }

    private var mSounds = HashMap<Sound, LoopMediaPlayer>()
    private var mSliderValues = HashMap<Sound, room106.asmr.player.models.SoundProperties>()
    private var mMixesList = MixesList()
    var playingMixID = -1

    private val VOLUMES_BY_DEFAULT = 0.8f
    private val STEREO_BY_DEFAULT = 0.5f
    val MAX_FAVORITES = 20
    val MAX_SOUNDS = 4

    fun initializeMediaPlayers(context: Context) {
        for (sound in Sound.values()) {
            mSounds[sound] = LoopMediaPlayer(context, sound.resID).apply {
                setVolume(VOLUMES_BY_DEFAULT, VOLUMES_BY_DEFAULT)
            }
            mSliderValues[sound] = SoundProperties(sound, VOLUMES_BY_DEFAULT, STEREO_BY_DEFAULT)
        }
    }

    fun isAbleToPlayOneMoreSound(): Boolean {
        return mSounds.filterValues { it.isPlaying() }.size < MAX_SOUNDS
    }

    fun play(sound: Sound) {
        mSounds[sound]?.play()
        playingMixID = -1
    }

    fun pause(sound: Sound) {
        mSounds[sound]?.pause()
        playingMixID = -1
    }

    fun isPlaying(sound: Sound): Boolean {
        return mSounds[sound]?.isPlaying() ?: false
    }

    fun setVolume(sound: Sound, leftVolume: Float, rightVolume: Float) {
        if (mSounds.containsKey(sound) && mSounds[sound] != null) {
            mSounds[sound]!!.setVolume(leftVolume, rightVolume)
        }
    }

    fun saveSlidersParameters(sound: Sound, volume: Float, stereo: Float) {
        mSliderValues[sound] = SoundProperties(sound, volume, stereo)
    }

    var currentMix: Mix
        get() {

            val mix = Mix()

            for ((sound, player) in mSounds) {
                if (player.isPlaying()) {
                    mix.add(sound, mSliderValues[sound])
                }
            }

            if (playingMixID == -1) {
                // Detect current mix on existed saved mix
                playingMixID = mMixesList.getMixID(mix)
            }

            return mix
        }
        private set(mixToPlay) {
            for ((sound, player) in mSounds) {

                val soundProperties = mixToPlay.getSound(sound)

                if (soundProperties != null) {
                    // Play sound
                    player.play()

                    mSliderValues[sound] = soundProperties
                } else {
                    // Pause sound
                    player.pause()
                }
            }
        }


    fun readMixesList(context: Context) {
        val mixesJSON = FileReader().readFavoriteMixesList(context)
        mMixesList = Gson().fromJson(mixesJSON, MixesList::class.java) ?: MixesList()
    }

    var mixesList: ArrayList<Mix>
        get () {
            return mMixesList.getList()
        }
        private set(value) {}

    fun isMixesListContains(mixToCheck: Mix): Boolean {
        return mMixesList.contains(mixToCheck)
    }

    fun saveCurrentMix(context: Context) {

        mMixesList.addMix(currentMix)
        val json = Gson().toJson(mMixesList)

        // Save mMixesList instance in favorites.json file
        FileReader().rewriteFavoriteMixesList(context, json)
    }

    fun playMix(mixID: Int) {

        // Save playing mix id
        if (playingMixID == mixID) {
            // Pressed on the same mix that is playing right now (Should be stopped)
            playingMixID = -1
            pauseAllSounds()
            return
        } else {
            playingMixID = mixID
        }

        currentMix = mMixesList.getList()[mixID]
    }

    fun getSlideValues(sound: Sound): room106.asmr.player.models.SoundProperties? {
        return mSliderValues[sound]
    }

    private fun pauseAllSounds() {
        for ((sound, player) in mSounds) {
            player.pause()
        }
    }

    private object HOLDER {
        val INSTANCE = ASMR()
    }

    companion object {
        val player: ASMR by lazy { HOLDER.INSTANCE }
    }
}
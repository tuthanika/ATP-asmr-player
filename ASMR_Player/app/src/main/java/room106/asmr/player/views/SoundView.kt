package room106.asmr.player.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.sound_layout.view.*
import room106.asmr.player.LoopMediaPlayer
import room106.asmr.player.R
import room106.asmr.player.activities.MainActivity
import room106.asmr.player.models.Sound
import java.lang.Thread.sleep


class SoundView: LinearLayout {

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
    private lateinit var mIconView: ImageButton
    private lateinit var mTitleView: TextView
    private lateinit var mPlayButton: ImageButton
    private lateinit var mControlPanel: LinearLayout
    private lateinit var mVolumeSeekBar: SeekBar
    private lateinit var mStereoSeekBar: SeekBar
    private lateinit var mSwitchDynamicStereo: Switch

    private var controlPanelIsVisible = false
    private var controlPanelMeasuredHeight = 0

    // Vars
    private var mTitle: String? = null

    // States
    private var isFree = false
    private var _isPlaying = false

    // Consts
    private val VOLUME_SEEKBAR_MAX = 100
    private val STEREO_SEEKBAR_MAX = 500

    // Sound
    private var loopMediaPlayer: LoopMediaPlayer? = null
    private val MIN_STEREO_SOUND = 0f
    private var dynamicIncreasing = true

    constructor(context: Context?,
                title: String,
                isFree: Boolean,
                mediaResource: Int,
                iconResource: Int): super(context) {

        View.inflate(context, R.layout.sound_layout, this)

        // Connect views
        mIconView = findViewById(R.id.soundIcon)
        mTitleView = findViewById(R.id.soundTitle)
        mControlPanel = findViewById(R.id.controlPanel)
        mPlayButton = findViewById(R.id.buttonPlay)
        mVolumeSeekBar = findViewById(R.id.seekBarVolume)
        mStereoSeekBar = findViewById(R.id.seekBarStereo)
        mSwitchDynamicStereo = findViewById(R.id.switchDynamicStereo)


        // Set control panel icon reset listeners
        findViewById<ImageButton>(R.id.controlPanelVolumeIcon).setOnClickListener(onClickResetVolumeListener)
        findViewById<ImageButton>(R.id.controlPanelStereoIcon).setOnClickListener(onClickResetStereoListener)
        findViewById<ImageButton>(R.id.controlPanelDynamicIcon).setOnClickListener(onClickResetDynamicListener)

        // Icon
        mIconView.setOnClickListener(onClickIconListener)
        mIconView.setImageResource(iconResource)

        // Title
        mTitle = title
        mTitleView.text = title

        // Play Button
        this.isFree = isFree
        mPlayButton.setOnClickListener(onClickPlayButton)

        if (isFree) {
            mPlayButton.setImageResource(R.drawable.ic_play)
        } else {
            mPlayButton.setImageResource(R.drawable.ic_lock)
        }

        // Control panel
        mControlPanel.visibility = View.INVISIBLE
        mControlPanel.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        controlPanelMeasuredHeight = mControlPanel.measuredHeight
        mVolumeSeekBar.setOnSeekBarChangeListener(onVolumeSeekBarChangeListener)
        mStereoSeekBar.setOnSeekBarChangeListener(onStereoSeekBarChangeListener)
        mSwitchDynamicStereo.setOnCheckedChangeListener(onDynamicStereoSwitchChange)

        // Create media player
        loopMediaPlayer =
            LoopMediaPlayer(context, mediaResource)

        // Update volume by default
        val volume = seekBarVolume.progress.toFloat() / VOLUME_SEEKBAR_MAX
        val stereo = seekBarStereo.progress.toFloat() / STEREO_SEEKBAR_MAX
        updateVolumeStereo(volume, stereo)
    }

    private val onClickIconListener = OnClickListener {
        controlPanelIsVisible = !controlPanelIsVisible

        if (controlPanelIsVisible) {
            // Show panel
            mControlPanel.visibility = View.VISIBLE

            val valueAnimator =
                ValueAnimator.ofInt(0, controlPanelMeasuredHeight)

            valueAnimator.addUpdateListener {
                val animatedValue = valueAnimator.animatedValue as Int
                val layoutParams = mControlPanel.layoutParams
                layoutParams.height = animatedValue
                mControlPanel.layoutParams = layoutParams
            }

            valueAnimator.duration = 300
            valueAnimator.start()

        } else {
            // Hide panel
            val valueAnimator =
                ValueAnimator.ofInt(controlPanelMeasuredHeight, 0)

            valueAnimator.addUpdateListener {
                val animatedValue = valueAnimator.animatedValue as Int
                val layoutParams = mControlPanel.layoutParams
                layoutParams.height = animatedValue
                mControlPanel.layoutParams = layoutParams
            }

            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mControlPanel.visibility = View.INVISIBLE

                    //  Hide guide
                    (context as MainActivity).hideGuidePanel()
                }
            })

            valueAnimator.duration = 300
            valueAnimator.start()
        }
    }

    private val onClickPlayButton = OnClickListener {

        // TODO - Add - "if (isFree || isProVersion)"
        if (isFree) {
            _isPlaying = !_isPlaying

            if (_isPlaying) {
                mPlayButton.setImageResource(R.drawable.ic_pause)

                // Play sound
                loopMediaPlayer?.start()
            } else {
                mPlayButton.setImageResource(R.drawable.ic_play)

                // Stop sound
                loopMediaPlayer?.pause()
            }
        }
    }

    private val onVolumeSeekBarChangeListener = object: SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(p0: SeekBar?) { }
        override fun onStopTrackingTouch(p0: SeekBar?) { }
        override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

            val volume = progress.toFloat() / VOLUME_SEEKBAR_MAX
            val stereo = seekBarStereo.progress.toFloat() / STEREO_SEEKBAR_MAX
            updateVolumeStereo(volume, stereo)
        }
    }

    private val onStereoSeekBarChangeListener = object: SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(p0: SeekBar?) { }
        override fun onStopTrackingTouch(p0: SeekBar?) { }
        override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

            val volume = seekBarVolume.progress.toFloat() / VOLUME_SEEKBAR_MAX
            val stereo = progress.toFloat() / STEREO_SEEKBAR_MAX
            updateVolumeStereo(volume, stereo)
        }
    }

    private val dynamicStereoRunnable = Runnable {
        while (mSwitchDynamicStereo.isChecked) {
            if (dynamicIncreasing) {
                seekBarStereo.progress += 1

                if (seekBarStereo.progress >= STEREO_SEEKBAR_MAX) {
                    dynamicIncreasing = false
                }
            } else {
                seekBarStereo.progress -= 1

                if (seekBarStereo.progress <= 0) {
                    dynamicIncreasing = true
                }
            }
            sleep(50)
        }
    }

    private val onDynamicStereoSwitchChange = CompoundButton.OnCheckedChangeListener {
            _: CompoundButton,
            isChecked: Boolean ->

        Log.d(TAG, "Switch changed: $isChecked")

        if (isChecked) {
            val dynamicStereoThread = Thread(dynamicStereoRunnable)
            dynamicStereoThread.start()
        }
    }

    private fun updateVolumeStereo(volume: Float, stereo: Float) {
        var leftVolume = volume * (1 - stereo)
        var rightVolume = volume * stereo

        if (leftVolume < MIN_STEREO_SOUND) {
            leftVolume = MIN_STEREO_SOUND
        }

        if (rightVolume < MIN_STEREO_SOUND) {
            rightVolume = MIN_STEREO_SOUND
        }

        loopMediaPlayer?.setVolume(leftVolume, rightVolume)
        Log.d(TAG,"Volume=$volume. Stereo=$stereo. L=$leftVolume R=$rightVolume")
    }


    fun getSoundObject(): Sound? {

        if (_isPlaying) {
            if (mTitle != null) {
                val volume = seekBarVolume.progress.toFloat() / VOLUME_SEEKBAR_MAX
                val stereo = seekBarStereo.progress.toFloat() / STEREO_SEEKBAR_MAX

                return Sound(mTitle!!, volume, stereo, switchDynamicStereo.isChecked)
            }
        }
        return null
    }

    fun isTitleEqual(title: String): Boolean {
        return mTitle.equals(title)
    }

    fun setSoundObject(sound: Sound) {
        mVolumeSeekBar.progress = (sound.volume * VOLUME_SEEKBAR_MAX).toInt()
        mStereoSeekBar.progress = (sound.stereo * STEREO_SEEKBAR_MAX).toInt()
        mSwitchDynamicStereo.isChecked = sound.isDynamic
        updateVolumeStereo(sound.volume, sound.stereo)
    }

    fun play() {
        _isPlaying = true
        mPlayButton.setImageResource(R.drawable.ic_pause)

        // Play sound
        loopMediaPlayer?.start()
    }

    fun pause() {
        _isPlaying = false
        mPlayButton.setImageResource(R.drawable.ic_play)

        // Stop sound
        loopMediaPlayer?.pause()
    }

    fun isPlaying(): Boolean {
        return _isPlaying
    }


    // Control Panel Icon Listeners
    private val onClickResetVolumeListener = OnClickListener {

        val progress = seekBarVolume.progress

        if (progress == 0) {
            seekBarVolume.progress = VOLUME_SEEKBAR_MAX
        } else {
            seekBarVolume.progress = 0
        }
    }

    private val onClickResetStereoListener = OnClickListener {
        seekBarStereo.progress = STEREO_SEEKBAR_MAX / 2
    }

    private val onClickResetDynamicListener = OnClickListener {
        switchDynamicStereo.isChecked = !switchDynamicStereo.isChecked
    }


    companion object {
        const val TAG = "SoundView"
    }

}
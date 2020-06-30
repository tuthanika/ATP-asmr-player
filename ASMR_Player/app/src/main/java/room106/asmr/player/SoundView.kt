package room106.asmr.player

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.sound_layout.view.*
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

    // States
    private var isFree = false
    private var isPlaying = false

    // Sound
    private var mediaPlayer: MediaPlayer? = null
    private val MIN_STEREO_SOUND = 0.1f
    private var dynamicIncreasing = true

    constructor(context: Context?,
                title: String,
                isFree: Boolean,
                mediaResource: Int): super(context) {

        View.inflate(context, R.layout.sound_layout, this)

        // Connect views
        mIconView = findViewById(R.id.soundIcon)
        mTitleView = findViewById(R.id.soundTitle)
        mControlPanel = findViewById(R.id.controlPanel)
        mPlayButton = findViewById(R.id.buttonPlay)
        mVolumeSeekBar = findViewById(R.id.seekBarVolume)
        mStereoSeekBar = findViewById(R.id.seekBarStereo)
        mSwitchDynamicStereo = findViewById(R.id.switchDynamicStereo)

        // Icon
        mIconView.setOnClickListener(onClickIconListener)

        // Title
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
        mediaPlayer = MediaPlayer.create(context, mediaResource)
        mediaPlayer?.isLooping = true
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
                }
            })

            valueAnimator.duration = 300
            valueAnimator.start()
        }
    }

    private val onClickPlayButton = OnClickListener {

        // TODO - Add - "if (isFree || isProVersion)"
        if (isFree) {
            isPlaying = !isPlaying

            if (isPlaying) {
                mPlayButton.setImageResource(R.drawable.ic_pause)

                // Play sound
                mediaPlayer?.start()
            } else {
                mPlayButton.setImageResource(R.drawable.ic_play)

                // Stop sound
                mediaPlayer?.pause()
            }
        }
    }

    private val onVolumeSeekBarChangeListener = object: SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(p0: SeekBar?) { }
        override fun onStopTrackingTouch(p0: SeekBar?) { }
        override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

            val volume = progress.toFloat() / 10
            val stereo = seekBarStereo.progress.toFloat() / 500
            updateVolumeStereo(volume, stereo)
        }
    }

    private val onStereoSeekBarChangeListener = object: SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(p0: SeekBar?) { }
        override fun onStopTrackingTouch(p0: SeekBar?) { }
        override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

            val volume = seekBarVolume.progress.toFloat() / 10
            val stereo = progress.toFloat() / 500
            updateVolumeStereo(volume, stereo)
        }
    }

    private val dynamicStereoRunnable = Runnable {
        while (mSwitchDynamicStereo.isChecked) {
            if (dynamicIncreasing) {
                seekBarStereo.progress += 1

                if (seekBarStereo.progress >= 500) {
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
    private val dynamicStereoThread = Thread(dynamicStereoRunnable)

    private val onDynamicStereoSwitchChange = CompoundButton.OnCheckedChangeListener {
            _: CompoundButton,
            isChecked: Boolean ->

        Log.d(TAG, "Switch changed: $isChecked")

        if (isChecked) {
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

        mediaPlayer?.setVolume(leftVolume, rightVolume)
        Log.d(TAG,"Volume=$volume. Stereo=$stereo. L=$leftVolume R=$rightVolume")
    }


    companion object {
        const val TAG = "SoundView"
    }

}
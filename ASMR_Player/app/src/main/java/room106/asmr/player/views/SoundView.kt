package room106.asmr.player.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.sound_layout.view.*
import room106.asmr.player.ASMR
import room106.asmr.player.R
import room106.asmr.player.activities.MainActivity
import java.lang.Thread.sleep

class SoundView : LinearLayout {

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
    private lateinit var mIconButton: ImageButton
    private lateinit var mTitleView: TextView
    private lateinit var mPlayButton: ImageButton
    private lateinit var mControlPanel: LinearLayout
    private lateinit var mVolumeSeekBar: SeekBar
    private lateinit var mStereoSeekBar: SeekBar
    private lateinit var mSwitchDynamicStereo: Switch

    // Bổ sung: Nút xóa custom sound (nếu layout có)
    private var mDeleteButton: ImageButton? = null

    private var controlPanelIsVisible = false
    private var controlPanelMeasuredHeight = 0

    // Vars
    private var mTitle: String? = null

    // States

    // Constants
    private val VOLUME_SEEKBAR_MAX = 100
    private val STEREO_SEEKBAR_MAX = 500

    // Sound
    private lateinit var mSound: ASMR.Sound
    private val MIN_STEREO_SOUND = 0f
    private var dynamicIncreasing = true

    constructor(
        context: Context?,
        sound: ASMR.Sound,
        iconResource: Int
    ) : super(context) {

        View.inflate(context, R.layout.sound_layout, this)

        // Connect views
        mIconButton = findViewById(R.id.soundIcon)
        mTitleView = findViewById(R.id.soundTitle)
        mControlPanel = findViewById(R.id.controlPanel)
        mPlayButton = findViewById(R.id.buttonPlay)
        mVolumeSeekBar = findViewById(R.id.seekBarVolume)
        mStereoSeekBar = findViewById(R.id.seekBarStereo)
        mSwitchDynamicStereo = findViewById(R.id.switchDynamicStereo)
        // Bổ sung: Tìm nút xóa (nếu có)
        mDeleteButton = findViewById(R.id.buttonDelete)

        // Set control panel icon reset listeners
        findViewById<ImageButton>(R.id.controlPanelVolumeIcon).setOnClickListener(onClickResetVolumeListener)
        findViewById<ImageButton>(R.id.controlPanelStereoIcon).setOnClickListener(onClickResetStereoListener)
        findViewById<ImageButton>(R.id.controlPanelDynamicIcon).setOnClickListener(onClickResetDynamicListener)

        // Icon
        mIconButton.setOnClickListener(onClickIconListener)
        mIconButton.setImageResource(iconResource)

        // Title
        mTitleView.text = sound.title

        mSound = sound

        // Play Button
        mPlayButton.setOnClickListener(onClickPlayButton)

        if (sound.isFree) {
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

        // Xử lý nút xóa custom sound (nếu có)
        mDeleteButton?.let { deleteBtn ->
            deleteBtn.visibility = if (isCustomSound(sound)) View.VISIBLE else View.GONE
            deleteBtn.setOnClickListener {
                onDeleteCustomSound()
            }
        }

        // Update volume by default
        val volume = seekBarVolume.progress.toFloat() / VOLUME_SEEKBAR_MAX
        val stereo = seekBarStereo.progress.toFloat() / STEREO_SEEKBAR_MAX
        updateVolumeStereo(volume, stereo)
    }

    // Xác định sound có phải custom không (bạn có thể sửa lại logic này)
    private fun isCustomSound(sound: ASMR.Sound): Boolean {
        // Giả sử custom sound có isFree = false hoặc check theo tên hoặc resource id
        // Bạn tùy chỉnh theo ý muốn
        return false // nếu muốn tắt nút xóa mặc định
    }

    // Hàm xử lý xóa custom sound (bạn cần bổ sung logic thực sự)
    private fun onDeleteCustomSound() {
        // TODO: Thực hiện logic xóa custom sound ở đây, ví dụ gọi MainActivity hoặc callback lên Activity
        Toast.makeText(context, "Delete custom sound!", Toast.LENGTH_SHORT).show()
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

        if (mSound.isFree) {

            if (ASMR.player.isPlaying(mSound)) {
                mPlayButton.setImageResource(R.drawable.ic_play)

                // Stop sound
                ASMR.player.pause(mSound)
            } else {
                if (ASMR.player.isAbleToPlayOneMoreSound()) {
                    mPlayButton.setImageResource(R.drawable.ic_pause)

                    // Play sound
                    ASMR.player.play(mSound)
                }
            }
        }
    }

    private val onVolumeSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(p0: SeekBar?) {}
        override fun onStopTrackingTouch(p0: SeekBar?) {}
        override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

            val volume = progress.toFloat() / VOLUME_SEEKBAR_MAX
            val stereo = seekBarStereo.progress.toFloat() / STEREO_SEEKBAR_MAX
            updateVolumeStereo(volume, stereo)
        }
    }

    private val onStereoSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(p0: SeekBar?) {}
        override fun onStopTrackingTouch(p0: SeekBar?) {}
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

    private val onDynamicStereoSwitchChange = CompoundButton.OnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->

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

        ASMR.player.setVolume(mSound, leftVolume, rightVolume)
        ASMR.player.saveSlidersParameters(mSound, volume, stereo)
    }

    fun updateSliderValues() {
        val sound = ASMR.player.getSlideValues(mSound)

        if (sound != null) {
            mVolumeSeekBar.progress = (sound.volume * VOLUME_SEEKBAR_MAX).toInt()
            mStereoSeekBar.progress = (sound.stereo * STEREO_SEEKBAR_MAX).toInt()
        }
    }

    fun updateIcon() {

        val image = if (ASMR.player.isPlaying(mSound)) {
            R.drawable.ic_pause
        } else {
            R.drawable.ic_play
        }
        mPlayButton.setImageResource(image)
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
}
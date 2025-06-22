package room106.asmr.player.views

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import room106.asmr.player.ASMR
import room106.asmr.player.R

class SoundView : LinearLayout {
    // Dùng cho custom sound
    private var customSound: ASMR.CustomSound? = null
    private var onDelete: ((ASMR.CustomSound?) -> Unit)? = null

    // Gốc cho sound mặc định
    private lateinit var mTitleView: TextView
    private lateinit var mSeekBar: SeekBar
    private lateinit var mIconButton: ImageButton
    private var soundIndex: Int = -1

    // Constructor cho custom sound
    constructor(
        context: Context,
        customSound: ASMR.CustomSound,
        iconResource: Int,
        onDelete: ((ASMR.CustomSound?) -> Unit)? = null
    ) : super(context) {
        View.inflate(context, R.layout.sound_layout, this)
        this.customSound = customSound
        this.onDelete = onDelete

        val mIconButton = findViewById<ImageButton>(R.id.soundIcon)
        val mTitleView = findViewById<TextView>(R.id.soundTitle)
        val mDeleteButton = findViewById<ImageButton>(R.id.buttonDelete)
        mTitleView.text = customSound.title
        mIconButton.setImageResource(iconResource)

        mIconButton.setOnClickListener {
            mDeleteButton.visibility = if (mDeleteButton.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        mDeleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Xoá âm thanh")
                .setMessage("Bạn chắc chắn muốn xoá?")
                .setPositiveButton("Xoá") { _, _ ->
                    onDelete?.invoke(customSound)
                }
                .setNegativeButton("Huỷ", null)
                .show()
        }
        mDeleteButton.visibility = View.GONE
    }

    // Constructor cho sound mặc định (gốc)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        View.inflate(context, R.layout.sound_layout, this)
        mTitleView = findViewById(R.id.soundTitle)
        mSeekBar = findViewById(R.id.soundSeekBar)
        mIconButton = findViewById(R.id.soundIcon)
        val mDeleteButton = findViewById<ImageButton>(R.id.buttonDelete)

        // Logic gốc: setup các sự kiện, thiết lập UI cho sound mặc định
        mDeleteButton.visibility = View.GONE // Không dùng với sound mặc định

        // Nếu có các logic gốc cho mSeekBar, mIconButton, v.v. thì giữ nguyên tại đây
        // Ví dụ:
        // mSeekBar.setOnSeekBarChangeListener(...)
        // mIconButton.setOnClickListener {...}
    }

    constructor(context: Context) : super(context)

    // Các hàm gốc khác nếu có, ví dụ: setSoundIndex, setVolume, v.v.
    fun setSoundIndex(index: Int) {
        this.soundIndex = index
    }

    fun setTitle(title: String) {
        mTitleView.text = title
    }

    fun setIcon(resId: Int) {
        mIconButton.setImageResource(resId)
    }

    fun setVolume(volume: Int) {
        mSeekBar.progress = volume
    }

    fun getSeekBar(): SeekBar = mSeekBar
}
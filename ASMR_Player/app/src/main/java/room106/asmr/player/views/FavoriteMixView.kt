package room106.asmr.player.views

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import room106.asmr.player.ASMR
import room106.asmr.player.R
import room106.asmr.player.activities.FavoritesActivity

class FavoriteMixView : LinearLayout {

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
    private lateinit var mMixTitleTextView: TextView
    private lateinit var mPlayButton: ImageButton
    // Bổ sung: Nút xóa và nút đổi tên
    private var mDeleteMixButton: ImageButton? = null
    private var mRenameMixButton: ImageButton? = null

    private var mixID = -1

    constructor(context: Context?, mixID: Int) : super(context) {
        View.inflate(context, R.layout.favorite_mix_layout, this)

        // Connect views
        mMixTitleTextView = findViewById(R.id.mixTitleTextView)
        mPlayButton = findViewById(R.id.playMixButton)
        mDeleteMixButton = findViewById(R.id.deleteMixButton)
        mRenameMixButton = findViewById(R.id.renameMixButton)

        this.mixID = mixID

        // Lấy tên mix (nếu có), nếu không thì dùng mặc định
        val name = try {
            ASMR.player.mixesList[mixID].name
        } catch (e: Exception) {
            null
        }
        mMixTitleTextView.text = name ?: resources.getString(R.string.mix_title_attr, mixID + 1)

        mPlayButton.setOnClickListener {
            ASMR.player.playMix(mixID)
            (context as FavoritesActivity).updateMixesIcons()
            context.checkSaveCurrentMixButton()
        }

        // Bổ sung: xử lý xóa mix
        mDeleteMixButton?.let { btn ->
            btn.visibility = View.VISIBLE
            btn.setOnClickListener {
                showDeleteMixDialog()
            }
        }

        // Bổ sung: xử lý đổi tên mix
        mRenameMixButton?.let { btn ->
            btn.visibility = View.VISIBLE
            btn.setOnClickListener {
                showRenameMixDialog()
            }
        }
    }

    fun updateIcon() {
        val image = if (ASMR.player.playingMixID == mixID) {
            R.drawable.ic_pause
        } else {
            R.drawable.ic_play
        }
        mPlayButton.setImageResource(image)
    }

    // Bổ sung: Hiển thị dialog xác nhận xóa mix
    private fun showDeleteMixDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.delete_mix_title)
            .setMessage(R.string.delete_mix_message)
            .setPositiveButton(R.string.delete_mix_confirm) { _, _ ->
                ASMR.player.deleteMix(context, mixID)
                (context as FavoritesActivity).updateMixesIcons()
                context.checkSaveCurrentMixButton()
                context.recreate() // Đảm bảo list view cập nhật lại đúng index
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    // Bổ sung: Hiển thị dialog đổi tên mix
    private fun showRenameMixDialog() {
        val editText = EditText(context)
        editText.setText(ASMR.player.mixesList[mixID].name)
        AlertDialog.Builder(context)
            .setTitle(R.string.rename_mix_title)
            .setView(editText)
            .setPositiveButton(R.string.rename_mix_confirm) { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    ASMR.player.renameMix(context, mixID, newName)
                    mMixTitleTextView.text = newName
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}
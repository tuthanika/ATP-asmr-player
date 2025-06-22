package room106.asmr.player.views

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import room106.asmr.player.ASMR
import room106.asmr.player.R
import room106.asmr.player.activities.FavoritesActivity

class FavoriteMixView(context: Context, val mixID: Int) : LinearLayout(context) {
    private val mMixTitleTextView: TextView
    private val mPlayButton: ImageButton
    private val mDeleteButton: ImageButton
    private val mRenameButton: ImageButton

    init {
        View.inflate(context, R.layout.favorite_mix_layout, this)
        mMixTitleTextView = findViewById(R.id.mixTitleTextView)
        mPlayButton = findViewById(R.id.playMixButton)
        mDeleteButton = findViewById(R.id.buttonDeleteMix)
        mRenameButton = findViewById(R.id.buttonRenameMix)

        mMixTitleTextView.text = ASMR.player.mixesList.getMixName(mixID)

        mPlayButton.setOnClickListener {
            ASMR.player.playMix(mixID)
            (context as? FavoritesActivity)?.updateMixesIcons()
            (context as? FavoritesActivity)?.checkSaveCurrentMixButton()
        }
        mDeleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Xoá mix")
                .setMessage("B?n mu?n xoá mix này?")
                .setPositiveButton("Xoá") { _, _ ->
                    ASMR.player.mixesList.removeAt(context, mixID)
                    (context as? FavoritesActivity)?.updateMixesListView()
                }
                .setNegativeButton("Hu?", null)
                .show()
        }
        mRenameButton.setOnClickListener {
            val editText = EditText(context)
            AlertDialog.Builder(context)
                .setTitle("Ð?i tên mix")
                .setView(editText)
                .setPositiveButton("Luu") { _, _ ->
                    ASMR.player.mixesList.renameMix(context, mixID, editText.text.toString())
                    mMixTitleTextView.text = editText.text.toString()
                    (context as? FavoritesActivity)?.updateMixesListView()
                }
                .setNegativeButton("Hu?", null)
                .show()
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
}
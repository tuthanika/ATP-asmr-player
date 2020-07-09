package room106.asmr.player.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import room106.asmr.player.ASMR
import room106.asmr.player.R
import room106.asmr.player.activities.FavoritesActivity

class FavoriteMixView: LinearLayout {

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


    private var mixID = -1

    constructor(context: Context?, mixID: Int): super(context) {
        View.inflate(context,
            R.layout.favorite_mix_layout, this)

        // Connect views
        mMixTitleTextView = findViewById(R.id.mixTitleTextView)
        mPlayButton = findViewById(R.id.playMixButton)

        this.mixID = mixID

        mMixTitleTextView.text = resources.getString(R.string.mix_title_attr, mixID + 1)
        mPlayButton.setOnClickListener {
            ASMR.player.playMix(mixID)

            (context as FavoritesActivity).updateMixesIcons()
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
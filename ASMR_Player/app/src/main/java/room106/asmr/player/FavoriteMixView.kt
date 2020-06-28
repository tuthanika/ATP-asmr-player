package room106.asmr.player

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

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

    constructor(context: Context?, mixTitle: String): super(context) {
        View.inflate(context, R.layout.favorite_mix_layout, this)

        // Connect views
        mMixTitleTextView = findViewById(R.id.mixTitleTextView)


        mMixTitleTextView.text = mixTitle


    }

}
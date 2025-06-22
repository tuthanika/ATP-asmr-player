package room106.asmr.player.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import room106.asmr.player.ASMR
import room106.asmr.player.R
import room106.asmr.player.views.FavoriteMixView


class FavoritesActivity : AppCompatActivity() {

    // Views
    private lateinit var mFavoriteMixesLinearLayout: LinearLayout
    private lateinit var mSaveCurrentMixButton: Button
    private lateinit var mEmptyFavoritesListTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Connect views
        mFavoriteMixesLinearLayout = findViewById(R.id.favoriteMixesList)
        mSaveCurrentMixButton = findViewById(R.id.saveCurrentMixButton)
        mEmptyFavoritesListTextView = findViewById(R.id.emptyFavoritesListTextView)

        ASMR.player.readMixesList(this)

        checkSaveCurrentMixButton()
        updateMixesListView()
    }

    // Show/Hide "Save current mix"
    fun checkSaveCurrentMixButton() {
        val currentMix = ASMR.player.currentMix
        val isNotSingle = currentMix.isNotSingleSound()
        val isContains = ASMR.player.isMixesListContains(currentMix)
        val isLessThanMax = ASMR.player.mixesList.size < ASMR.player.MAX_FAVORITES

        mSaveCurrentMixButton.visibility = if (isNotSingle && !isContains && isLessThanMax) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    // Click "Save current mix"
    fun onClickSaveCurrent(v: View) {
        ASMR.player.saveCurrentMix(this)
        mSaveCurrentMixButton.visibility = View.GONE
        checkSaveCurrentMixButton()
        updateMixesListView()
    }

    // Update Mixes List
    private fun updateMixesListView() {

        val list = ASMR.player.mixesList
        mFavoriteMixesLinearLayout.removeAllViews()

        if (list.isEmpty()) {
            mEmptyFavoritesListTextView.visibility = View.VISIBLE
        } else {
            mEmptyFavoritesListTextView.visibility = View.GONE

            for (i in 0 until list.size) {
                val mixView = FavoriteMixView(this, i)
                mFavoriteMixesLinearLayout.addView(mixView)
            }

            updateMixesIcons()
        }
    }

    // Update Play buttons
    fun updateMixesIcons() {
        for (i in 0 until mFavoriteMixesLinearLayout.childCount) {
            (mFavoriteMixesLinearLayout.getChildAt(i) as FavoriteMixView).updateIcon()
        }
    }

    fun onClickBack(v: View) {
        finish()
        overridePendingTransition(
            R.anim.freeze,
            R.anim.slide_out_left
        )
    }
}
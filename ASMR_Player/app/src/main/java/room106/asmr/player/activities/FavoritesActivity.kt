package room106.asmr.player.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import room106.asmr.player.FileReader
import room106.asmr.player.R
import room106.asmr.player.models.Mix
import room106.asmr.player.models.MixesList
import room106.asmr.player.views.FavoriteMixView


class FavoritesActivity : AppCompatActivity() {

    // Views
    private lateinit var mFavoriteMixesLinearLayout: LinearLayout
    private lateinit var mSaveCurrentMixButton: Button
    private lateinit var mEmptyFavoritesListTextView: TextView

    private var mCurrentMix: Mix? = null
    private var mMixesList: MixesList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Connect views
        mFavoriteMixesLinearLayout = findViewById(R.id.favoriteMixesList)
        mSaveCurrentMixButton = findViewById(R.id.saveCurrentMixButton)
        mEmptyFavoritesListTextView = findViewById(R.id.emptyFavoritesListTextView)


        if (intent.extras!!.containsKey("currentMixJSON")) {
            val json = intent.extras!!.getString("currentMixJSON")
            mCurrentMix = Gson().fromJson(json, Mix::class.java)

            // Hide or show Save Current Mix button according to sound that playing at that moment
            if (mCurrentMix != null) {
                if (mCurrentMix!!.isNotSingleSound()) {
                    mSaveCurrentMixButton.visibility = View.VISIBLE
                } else {
                    mSaveCurrentMixButton.visibility = View.GONE
                }
            }
        }

        val mixesJSON = FileReader().readFavoriteMixesList(this)
        if (mixesJSON != null) {
            Log.d("JSON", "JSON from favorites.json is: $mixesJSON")
            mMixesList = Gson().fromJson(mixesJSON, MixesList::class.java)
        }

        if (mMixesList == null) {
            mMixesList = MixesList()
        }

        updateMixesListView()
    }

    private fun checkMixesListSize() {
        if (mMixesList == null) { return }

        if (mMixesList!!.isEmpty()) {
            mEmptyFavoritesListTextView.visibility = View.VISIBLE
        } else {
            mEmptyFavoritesListTextView.visibility = View.GONE
        }
    }

    fun onClickSaveCurrent(v: View) {
        if (mMixesList != null && mCurrentMix != null) {
            mMixesList!!.addMix(mCurrentMix!!)

            val mixesListJSON = Gson().toJson(mMixesList)
            Log.d("JSON", "mixesListJSON: $mixesListJSON")

            FileReader().rewriteFavoriteMixesList(this, mixesListJSON)
            mSaveCurrentMixButton.visibility = View.GONE
            updateMixesListView()
        }
    }

    private fun updateMixesListView() {
        if (mMixesList == null) return

        // Show or Hide "Empty list" TextView
        checkMixesListSize()

        mFavoriteMixesLinearLayout.removeAllViews()

        val list = mMixesList!!.getList()

        for (i in 0 until list.size) {
            val mixView =
                FavoriteMixView(this, "Mix #${i + 1}")
            mixView.setMix(list[i])

            mFavoriteMixesLinearLayout.addView(mixView)
        }

    }

    fun playMix(mix: Mix) {
        val mixJSON = Gson().toJson(mix)
        val i = Intent().putExtra("playMix", mixJSON)
        setResult(Activity.RESULT_OK, i)

        finish()
        overridePendingTransition(
            R.anim.freeze,
            R.anim.slide_out_left
        )
    }



    fun onClickBack(v: View) {
        setResult(Activity.RESULT_CANCELED)
        finish()
        overridePendingTransition(
            R.anim.freeze,
            R.anim.slide_out_left
        )
    }

}
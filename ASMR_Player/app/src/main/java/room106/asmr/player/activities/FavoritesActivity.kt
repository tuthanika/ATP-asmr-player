package room106.asmr.player.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
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

    private var mCurrentMix: Mix? = null
    private var mMixesList: MixesList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        if (intent.extras!!.containsKey("currentMixJSON")) {
            val json = intent.extras!!.getString("currentMixJSON")
            mCurrentMix = Gson().fromJson(json, Mix::class.java)
        }

        // Connect views
        mFavoriteMixesLinearLayout = findViewById(R.id.favoriteMixesList)

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

    fun onClickSaveCurrent(v: View) {
        if (mMixesList != null && mCurrentMix != null) {
            mMixesList!!.addMix(mCurrentMix!!)

            val mixesListJSON = Gson().toJson(mMixesList)
            Log.d("JSON", "mixesListJSON: $mixesListJSON")

            FileReader()
                .rewriteFavoriteMixesList(this, mixesListJSON)
        }

        updateMixesListView()
    }

    private fun updateMixesListView() {

        if (mMixesList == null) return

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
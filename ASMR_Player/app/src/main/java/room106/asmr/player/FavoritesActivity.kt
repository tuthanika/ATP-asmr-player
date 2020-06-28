package room106.asmr.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout

class FavoritesActivity : AppCompatActivity() {

    // Views
    private lateinit var mFavoriteMixesLinearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Connect views
        mFavoriteMixesLinearLayout = findViewById(R.id.favoriteMixesList)

        val mix1 = FavoriteMixView(this, "Mix #1")
        val mix2 = FavoriteMixView(this, "Mix #2")
        val mix3 = FavoriteMixView(this, "Mix #3")

        mFavoriteMixesLinearLayout.addView(mix1)
        mFavoriteMixesLinearLayout.addView(mix2)
        mFavoriteMixesLinearLayout.addView(mix3)
    }

    fun onClickBack(v: View) {
        finish()
        overridePendingTransition(R.anim.freeze, R.anim.slide_out_left)
    }

}
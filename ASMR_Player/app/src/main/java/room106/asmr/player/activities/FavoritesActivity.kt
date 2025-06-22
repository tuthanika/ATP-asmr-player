package room106.asmr.player.activities

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import room106.asmr.player.ASMR
import room106.asmr.player.R
import room106.asmr.player.views.FavoriteMixView

class FavoritesActivity : AppCompatActivity() {
    private lateinit var mixesListLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        mixesListLayout = findViewById(R.id.mixesListLayout)
        updateMixesListView()
    }

    // Cập nhật danh sách mix yêu thích (UI)
    fun updateMixesListView() {
        mixesListLayout.removeAllViews()
        val mixes = ASMR.player.mixesList.getList()
        for ((index, _) in mixes.withIndex()) {
            val mixView = FavoriteMixView(this, index)
            mixesListLayout.addView(mixView)
        }
    }

    // Cập nhật icon Play/Pause cho các FavoriteMixView
    fun updateMixesIcons() {
        for (i in 0 until mixesListLayout.childCount) {
            val child = mixesListLayout.getChildAt(i)
            if (child is FavoriteMixView) {
                child.updateIcon()
            }
        }
    }

    // Nếu có nút lưu trạng thái mix hiện tại, cập nhật trạng thái nút này tại đây
    fun checkSaveCurrentMixButton() {
        // Tùy vào giao diện gốc, có thể hiện hoặc ẩn nút lưu mix hiện tại
        // (hoặc cập nhật trạng thái enabled/disabled, v.v.)
    }
}
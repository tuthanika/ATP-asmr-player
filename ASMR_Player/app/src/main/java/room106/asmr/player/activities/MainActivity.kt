package room106.asmr.player.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import room106.asmr.player.ASMR
import room106.asmr.player.R
import room106.asmr.player.views.SoundView

class MainActivity : AppCompatActivity() {
    // Các thuộc tính UI gốc
    private lateinit var customSoundsListLayout: LinearLayout
    private var currentCategory: String = "Natural"
    private val REQUEST_CODE_PICK_AUDIO = 1001

    // --- Các thuộc tính khác của app gốc ---
    // Thêm các thuộc tính khác của MainActivity ở đây (nếu có)
    // Ví dụ:
    // private lateinit var soundsListLayout: LinearLayout
    // ... các biến khác cho UI, slider, v.v.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo UI gốc
        // soundsListLayout = findViewById(R.id.soundsListLayout)
        // ... các dòng khởi tạo UI khác của app gốc

        // Khởi tạo custom sound UI
        customSoundsListLayout = findViewById(R.id.customSoundsListLayout)
        updateCustomSoundsListUI()

        // Gọi khởi tạo player, load mixes, v.v. nếu có
        // ASMR.player.initializeMediaPlayers(this)
        // ASMR.player.readMixesList(this)
        // ... các logic gốc khác
    }

    // --- CUSTOM SOUND UI MANAGEMENT (BỔ SUNG) ---
    private fun updateCustomSoundsListUI() {
        customSoundsListLayout.removeAllViews()
        val sounds = ASMR.player.getCustomSounds(currentCategory)
        sounds.forEach { sound ->
            val soundView = SoundView(this, sound, R.drawable.ic_tmp_icon) { csound ->
                if (csound != null) {
                    ASMR.player.removeCustomSound(csound)
                    updateCustomSoundsListUI()
                }
            }
            customSoundsListLayout.addView(soundView)
        }
    }

    fun onClickAddSound(view: android.view.View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_AUDIO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_AUDIO && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                showCategoryDialog(uri)
            }
        }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        var result = "Custom Sound"
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val idx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (idx >= 0) result = it.getString(idx) ?: result
                }
            }
        }
        return result
    }

    private fun showCategoryDialog(uri: Uri) {
        val categories = ASMR.player.getCategories().toMutableList()
        categories.add("Tạo danh mục mới...")
        AlertDialog.Builder(this)
            .setTitle("Chọn danh mục")
            .setItems(categories.toTypedArray()) { _, which ->
                if (which == categories.size - 1) {
                    val input = EditText(this)
                    AlertDialog.Builder(this)
                        .setTitle("Tên danh mục mới")
                        .setView(input)
                        .setPositiveButton("Thêm") { _, _ ->
                            val newCat = input.text.toString()
                            ASMR.player.addCategory(newCat)
                            val title = getFileNameFromUri(uri)
                            ASMR.player.addCustomSound(uri, title, newCat)
                            currentCategory = newCat
                            updateCustomSoundsListUI()
                        }
                        .setNegativeButton("Huỷ", null)
                        .show()
                } else {
                    val title = getFileNameFromUri(uri)
                    ASMR.player.addCustomSound(uri, title, categories[which])
                    currentCategory = categories[which]
                    updateCustomSoundsListUI()
                }
            }.show()
    }
    // --- END CUSTOM SOUND UI MANAGEMENT ---

    // --- CÁC HÀM GỐC KHÁC CỦA MainActivity ---
    // Ví dụ:
    // fun onPlayButtonClicked(view: View) { ... }
    // fun onSliderChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) { ... }
    // ... tất cả các hàm, logic khác của MainActivity của app gốc ASMR Player
}
package room106.asmr.player.models

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class MixesList {
    private val mixes: ArrayList<Mix> = ArrayList()

    class Mix(var name: String, var volumes: List<Int>)

    fun addMix(name: String, volumes: List<Int>) {
        mixes.add(Mix(name, volumes))
    }

    fun removeAt(context: Context, index: Int) {
        if (index >= 0 && index < mixes.size) {
            mixes.removeAt(index)
            saveToPrefs(context)
        }
    }

    fun renameMix(context: Context, index: Int, newName: String) {
        if (index >= 0 && index < mixes.size) {
            mixes[index].name = newName
            saveToPrefs(context)
        }
    }

    fun getMixName(index: Int): String {
        if (index >= 0 && index < mixes.size) {
            return mixes[index].name
        }
        return ""
    }

    fun getMixVolumes(index: Int): List<Int> {
        if (index >= 0 && index < mixes.size) {
            return mixes[index].volumes
        }
        return emptyList()
    }

    fun getList(): List<Mix> {
        return mixes
    }

    fun size(): Int {
        return mixes.size
    }

    fun clear(context: Context) {
        mixes.clear()
        saveToPrefs(context)
    }

    fun loadFromPrefs(context: Context) {
        mixes.clear()
        val sharedPref = context.getSharedPreferences(PREFS_MIXES, Context.MODE_PRIVATE)
        val mixesJson = sharedPref.getString(KEY_MIXES, null)
        if (mixesJson != null) {
            val jsonArr = JSONArray(mixesJson)
            for (i in 0 until jsonArr.length()) {
                val obj = jsonArr.getJSONObject(i)
                val name = obj.getString("name")
                val volumesArr = obj.getJSONArray("volumes")
                val volumes = mutableListOf<Int>()
                for (j in 0 until volumesArr.length()) {
                    volumes.add(volumesArr.getInt(j))
                }
                mixes.add(Mix(name, volumes))
            }
        }
    }

    fun saveToPrefs(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_MIXES, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val jsonArr = JSONArray()
        for (mix in mixes) {
            val obj = JSONObject()
            obj.put("name", mix.name)
            val volumesArr = JSONArray()
            for (v in mix.volumes) {
                volumesArr.put(v)
            }
            obj.put("volumes", volumesArr)
            jsonArr.put(obj)
        }
        editor.putString(KEY_MIXES, jsonArr.toString())
        editor.apply()
    }

    companion object {
        private const val PREFS_MIXES = "asmr_player_mixes"
        private const val KEY_MIXES = "mixes"
    }
}
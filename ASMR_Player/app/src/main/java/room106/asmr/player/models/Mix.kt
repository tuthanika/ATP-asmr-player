package room106.asmr.player.models

class Mix {

    var title: String? = null
    private var mSounds = ArrayList<Sound>()

    fun add(sound: Sound) {
        mSounds.add(sound)
    }

    fun getSoundsList(): ArrayList<Sound> {
        return mSounds
    }

    fun isSoundExist(titleToCheck: String): Boolean {
        for (sound in mSounds) {
            if (sound.title == titleToCheck) {
                return true
            }
        }
        return false
    }

    fun isEmpty(): Boolean {
        return mSounds.size == 0
    }

    fun isNotSingleSound(): Boolean {
        return mSounds.size > 1
    }
}
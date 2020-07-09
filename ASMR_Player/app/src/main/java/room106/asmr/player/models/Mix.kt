package room106.asmr.player.models

import room106.asmr.player.ASMR

class Mix {

    private var mSounds = ArrayList<SoundProperties>()

    fun add(sound: SoundProperties?) {
        if (sound != null) {
            mSounds.add(sound)
        }
    }

    fun getSoundsList(): ArrayList<SoundProperties> {
        return mSounds
    }

    fun getSound(sound: ASMR.Sound): SoundProperties? {
        for (s in mSounds) {
            if (s.sound == sound) {
                return s
            }
        }
        return null
    }

    private fun isSoundExist(soundToCheck: ASMR.Sound): Boolean {
        for (sound in mSounds) {
            if (sound.sound == soundToCheck) {
                return true
            }
        }
        return false
    }

    fun isNotSingleSound(): Boolean {
        return mSounds.size > 1
    }

    override fun equals(other: Any?): Boolean {
        if (other is Mix) {
            val otherSoundList = other.getSoundsList()

            if (mSounds.size != otherSoundList.size) {
                return false
            }

            for (i in 0 until mSounds.size) {
//                if (mSounds[i].sound != otherSoundList[i].sound) {
//                    return false
//                }

                if (!isSoundExist(otherSoundList[i].sound)) {
                    return false
                }

            }
            return true
        }
        return false
    }
}
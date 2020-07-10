package room106.asmr.player.models

import room106.asmr.player.ASMR

class Mix {

//    private var mSounds = ArrayList<SoundProperties>()
    private var mSounds = HashMap<ASMR.Sound, SoundProperties>()

    fun add(sound: ASMR.Sound, soundProperties: SoundProperties?) {
        if (soundProperties != null) {
            mSounds[sound] = soundProperties
        }
    }

    fun getSoundsList(): HashMap<ASMR.Sound, SoundProperties> {
        return mSounds
    }

    fun getSound(sound: ASMR.Sound): SoundProperties? {
        return mSounds[sound]
    }

    private fun isSoundExist(soundToCheck: ASMR.Sound): Boolean {
        return mSounds.containsKey(soundToCheck)
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

            for ((sound, soundProperties) in mSounds) {
                if (!other.isSoundExist(sound)) {
                    return false
                }
            }
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        return mSounds.hashCode()
    }
}
package room106.asmr.player.models

class MixesList {

    private var mList = ArrayList<Mix>()

    fun addMix(mix: Mix) {
        mList.add(mix)
    }

    fun getList(): ArrayList<Mix> {
        return mList
    }

    fun isEmpty(): Boolean {
        return mList.size == 0
    }

    fun contains(mixToCheck: Mix): Boolean {

        for (mix in mList) {
            if (mix == mixToCheck) {
                return true
            }
        }

        return false
    }

}
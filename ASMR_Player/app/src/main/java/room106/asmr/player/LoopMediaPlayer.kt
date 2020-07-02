package room106.asmr.player

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener


internal class LoopMediaPlayer(private val context: Context?, private val resId: Int)  {

    private var mCurrentPlayer: MediaPlayer? = null
    private var mNextPlayer: MediaPlayer? = null

    private var leftVolume = 0f
    private var rightVolume = 0f

    private fun createNextMediaPlayer() {
        mNextPlayer = MediaPlayer.create(context, resId)
        mCurrentPlayer!!.setNextMediaPlayer(mNextPlayer)
        mCurrentPlayer!!.setOnCompletionListener(onCompletionListener)
    }

    private val onCompletionListener = OnCompletionListener { mediaPlayer ->
            mediaPlayer.release()
            mNextPlayer?.setVolume(leftVolume, rightVolume)
            mCurrentPlayer = mNextPlayer
            createNextMediaPlayer()
        }

    init {
        mCurrentPlayer = MediaPlayer.create(context, resId)
        createNextMediaPlayer()
    }

    fun start() {
        mCurrentPlayer?.start()
    }

    fun pause() {
        mCurrentPlayer?.pause()
    }

    fun setVolume(leftVolume: Float, rightVolume: Float) {
        this.leftVolume = leftVolume
        this.rightVolume = rightVolume
        mCurrentPlayer?.setVolume(leftVolume, rightVolume)
    }
}
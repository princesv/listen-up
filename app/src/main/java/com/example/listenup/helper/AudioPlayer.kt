package com.example.listenup.helper

import android.media.MediaPlayer
import android.media.PlaybackParams
import com.example.listenup.common.TimeConverter
import java.io.File

class AudioPlayer {
    var mediaPlayer: MediaPlayer? = null
    fun replayAudio(audioBytes: ByteArray,audioSpeed: Float) {
        val tempFile = File.createTempFile("modified_voice", ".mp3")
        tempFile.writeBytes(audioBytes)
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(tempFile.absolutePath)
            setOnPreparedListener {
                val params = PlaybackParams().apply {
                    speed = audioSpeed
                }
                it.playbackParams = params
                mediaPlayer!!.start()
            }
            prepareAsync()
        }
    }

    fun pauseAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    fun setupMediaPlayer(audioBytes: ByteArray, audioSpeed:Float,onDurationAvailable: (Long) -> Unit) {
        val tempFile = File.createTempFile("modified_voice", ".mp3")
        tempFile.writeBytes(audioBytes)
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(tempFile.absolutePath)
            setOnPreparedListener {
                val params = PlaybackParams().apply {
                    speed = audioSpeed
                }
                it.playbackParams = params
                it.pause() // Force pause if it starts unexpectedly
                it.seekTo(0)
                onDurationAvailable(mediaPlayer!!.duration.toLong())
            }
            prepareAsync()
        }
    }

    fun resumeAudio(audioBytes: ByteArray,audioSpeed: Float) {
        if(mediaPlayer == null){
            val tempFile = File.createTempFile("modified_voice", ".mp3")
            tempFile.writeBytes(audioBytes)
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(tempFile.absolutePath)
                setOnPreparedListener {
                    val params = PlaybackParams().apply {
                        speed = audioSpeed
                    }
                    it.playbackParams = params
                    mediaPlayer!!.start()
                }
                prepareAsync()
            }
        }else {
            mediaPlayer?.let {
                if (!it.isPlaying) {
                    it.start()
                }
            }
        }
    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
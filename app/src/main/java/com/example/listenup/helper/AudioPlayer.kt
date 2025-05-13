package com.example.listenup.helper

import android.media.MediaPlayer
import com.example.listenup.common.TimeConverter
import java.io.File

class AudioPlayer {
    var mediaPlayer: MediaPlayer? = null
    fun replayAudio(audioBytes: ByteArray) {
        val tempFile = File.createTempFile("modified_voice", ".mp3")
        tempFile.writeBytes(audioBytes)
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(tempFile.absolutePath)
            setOnPreparedListener {
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

    fun setupMediaPlayer(audioBytes: ByteArray,onDurationAvailable: (String) -> Unit) {
        val tempFile = File.createTempFile("modified_voice", ".mp3")
        tempFile.writeBytes(audioBytes)
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(tempFile.absolutePath)
            setOnPreparedListener {
                onDurationAvailable(TimeConverter.getFormattedTimeFromMillis(mediaPlayer!!.duration.toLong()))
            }
            prepareAsync()
        }
    }

    fun resumeAudio() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
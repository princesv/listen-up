package com.example.listenup.helper

import android.media.MediaPlayer
import java.io.File

class AudioPlayer {
    fun playAudio(audioBytes: ByteArray) {
        val tempFile = File.createTempFile("modified_voice", ".mp3")
        tempFile.writeBytes(audioBytes)

        MediaPlayer().apply {
            setDataSource(tempFile.absolutePath)
            prepare()
            start()
        }
    }
}
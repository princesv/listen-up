package com.example.listenup.helper

import android.media.MediaRecorder
import java.io.File

class AudioRecorder {
    fun startRecording(outputFile: File) {
        val mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(outputFile.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            prepare()
            start()
        }
    }
}
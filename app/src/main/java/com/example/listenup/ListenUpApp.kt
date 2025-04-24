package com.example.listenup

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.listenup.api.ElevenLabsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListenUpApp: Application() {
    lateinit var elevenLabsApi:ElevenLabsApi
    lateinit var speechRecognizer:SpeechRecognizer
    @Override
    override fun onCreate() {
        super.onCreate()
        instance=this
        createElevenLabsApi()
        setupSpeechRecognizer()
    }
    fun createElevenLabsApi(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.elevenlabs.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        elevenLabsApi= retrofit.create(ElevenLabsApi::class.java)
    }
    fun setupSpeechRecognizer(){
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d("SpeechRecognizer", "Ready for speech")
                Toast.makeText(instance, "Listening started", Toast.LENGTH_SHORT).show()
            }

            override fun onBeginningOfSpeech() {
                Log.d("SpeechRecognizer", "Speech has begun")
                // Optional: update UI to indicate user has started speaking
            }

            override fun onRmsChanged(rmsdB: Float) {
                // This value changes based on user's voice volume
                // Optional: update visual waveform/mic animation
                Log.d("SpeechRecognizer", "RMS dB: $rmsdB")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                Log.d("SpeechRecognizer", "Audio buffer received")
                // Usually ignored unless processing audio data manually
            }

            override fun onEndOfSpeech() {
                Log.d("SpeechRecognizer", "Speech ended")
                Toast.makeText(instance, "Processing speech...", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: Int) {
                val errorMsg = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    else -> "Unknown error"
                }

                Log.e("SpeechRecognizer", "Error occurred: $errorMsg ($error)")
                Toast.makeText(instance, "Speech error: $errorMsg", Toast.LENGTH_LONG).show()
            }

            override fun onResults(results: Bundle) {
                val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.get(0) ?: ""
                Log.d("SpeechRecognizer", "Final result: $text")

                // 🎯 Send this text to ElevenLabs or update UI
                // sendToElevenLabs(text)
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                partial?.firstOrNull()?.let {
                    Log.d("SpeechRecognizer", "Partial result: $it")
                    // Optional: show real-time transcription
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                Log.d("SpeechRecognizer", "Event: $eventType")
                // Rarely used — can usually be left empty
            }


        })
    }
    companion object {
        private lateinit var instance: ListenUpApp
        fun getAppInstance(): ListenUpApp = instance
    }
}
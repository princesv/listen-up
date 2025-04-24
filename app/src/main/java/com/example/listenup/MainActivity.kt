package com.example.listenup

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.listenup.helper.AudioPlayer
import com.example.listenup.helper.ElevenLabs
import com.example.listenup.viewModels.VoiceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import android.Manifest
import java.util.Locale


class MainActivity : AppCompatActivity() {
    var audioPlayer:AudioPlayer= AudioPlayer()
    lateinit var elevenLabs: ElevenLabs
    val voiceViewModel: VoiceViewModel by viewModels()
    val speechRecognizer:SpeechRecognizer=ListenUpApp.getAppInstance().speechRecognizer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        askAudioPermission()
        lifecycleScope.launch {
            voiceViewModel.generateVoice("Wassup bitch!!!","21m00Tcm4TlvDq8ikWAM")
            voiceViewModel.audioData.collect(){state->
                audioPlayer.playAudio(state!!)
            }
        }
        lifecycleScope.launch {
            delay(1000)
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            }
            speechRecognizer.startListening(intent)
            Toast.makeText(ListenUpApp.getAppInstance(),"Started",Toast.LENGTH_SHORT).show()
            delay(8000)
            Toast.makeText(ListenUpApp.getAppInstance(),"Stopped",Toast.LENGTH_SHORT).show()
            speechRecognizer.stopListening()
        }


    }
    fun askAudioPermission(){
        val RECORD_AUDIO_REQUEST_CODE = 101

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
        }

    }

}
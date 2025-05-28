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
import android.content.Context
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.listenup.languageModel.OkHttpModelDownloader
import com.example.listenup.languageModel.TranscriptResult
import com.example.listenup.languageModel.VoskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import java.lang.Exception
import java.util.Locale
import kotlin.math.log


class MainActivity : FragmentActivity(){
    //  var audioPlayer:AudioPlayer= AudioPlayer()
    //  lateinit var elevenLabs: ElevenLabs
    val voiceViewModel: VoiceViewModel by viewModels()
    lateinit var speechService:SpeechService
    var firstFinalVostListeningResult = false

    //  val speechRecognizer:SpeechRecognizer=ListenUpApp.getAppInstance().speechRecognizer
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
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setUpRecordButtonToListenVosk()
    }

    fun askAudioPermission() {
        val RECORD_AUDIO_REQUEST_CODE = 101

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
        }

    }

    fun setUpRecordButtonToListenVosk(){
        voiceViewModel.isRunning.observe(this,{
            if(it){
                if(voiceViewModel.selectedLanguageModel.value != VoskModel.Default)
                    startListeningVosk()
            }else{
                if(voiceViewModel.selectedLanguageModel.value != VoskModel.Default)
                    stopListeningVosk()
            }
        })
    }

    fun startListeningVosk(){
        voiceViewModel.finalTextResult=""
        voiceViewModel._textTranslated.value=""
        lifecycleScope.launch {
            val modelFile = File(filesDir, VoskModel.Hindi.dirName)
            if (modelFile != null) {
                setupVoskSpeechService(modelFile,VoskModel.Hindi)
            } else {
                Toast.makeText(this@MainActivity, "Model load failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setupVoskSpeechService(modelPath: File,model:VoskModel) {
        val extractedPath= File(modelPath,model.modelFolderName)
        val modelFile = Model(extractedPath.absolutePath)
        val recognizer = Recognizer(modelFile, 16000.0f)
        speechService = SpeechService(recognizer, 16000.0f)
        speechService.startListening(object : RecognitionListener {
            override fun onPartialResult(hypothesis: String?) {
            /* update UI */
                Log.d("VOSK", hypothesis?:"")
                voiceViewModel._textTranslated.value=TranscriptResult.getPartialResult(hypothesis?:"{\n" +"\"partial\" : \"\"" +"}")
            }

            override fun onResult(hypothesis: String?) { /* final result */
                Log.d("VOSK", hypothesis?:"")
                voiceViewModel.finalTextResult+=" "+TranscriptResult.getFinalResult(hypothesis?:"{\n" +"\"text\" : \"\"" +"}")
                Log.d("VOSK final result", voiceViewModel.finalTextResult)
                voiceViewModel._textTranslated.value=voiceViewModel.finalTextResult
                firstFinalVostListeningResult = true
            }

            override fun onFinalResult(hypothesis: String?) {}
            override fun onError(exception: java.lang.Exception?) {}
            override fun onTimeout() {}
        })
    }
    fun stopListeningVosk(){
        lifecycleScope.launch {
            speechService.stop()
            delay(500)
            if(firstFinalVostListeningResult) {
                voiceViewModel.finalTextResult += " " + voiceViewModel._textTranslated.value
                Log.d("VOSK final result", voiceViewModel.finalTextResult)
                voiceViewModel._textTranslated.value = voiceViewModel.finalTextResult
            }
        }

    }
}

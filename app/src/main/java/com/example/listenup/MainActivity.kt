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
    lateinit var progressBar: ProgressBar
    lateinit var progressPercentage: TextView
    lateinit var speechService:SpeechService

    //  val speechRecognizer:SpeechRecognizer=ListenUpApp.getAppInstance().speechRecognizer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.languageDownloaderProgress)
        progressPercentage = findViewById(R.id.progressPercentage)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        askAudioPermission()
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        downloadHindiLanguageModel()
        lifecycleScope.launch {
            val modelFile = File(filesDir, VoskModel.English.dirName)
            if (modelFile != null) {
                startListeningVoskVosk(this@MainActivity, modelFile)
            } else {
                Toast.makeText(this@MainActivity, "Model load failed", Toast.LENGTH_LONG).show()
            }
        }


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

    fun downloadHindiLanguageModel() {
        val model = VoskModel.English

        if (OkHttpModelDownloader.isModelDownloaded(this, model)) {
            //  initRecognizer(model)
           // initializeSpeechService(model.dirName)
            val a=2
        } else {
            OkHttpModelDownloader.downloadModel(
                context = this,
                model = model,
                onProgress = { percent ->
                    runOnUiThread {
                        progressBar.progress = percent
                        progressPercentage.text = "$percent%"
                    }
                },
                onComplete = {
                    runOnUiThread {
                        Toast.makeText(this, "Download complete", Toast.LENGTH_SHORT).show()
                        //   initRecognizer(model)
                       // initializeSpeechService(model.dirName)
                    }
                },
                onError = { e ->
                    runOnUiThread {
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }

    fun startListeningVoskVosk(context: Context, modelPath: File) {
        val extractedPath= File(modelPath,"vosk-model-small-en-us-0.15")
        val model = Model(extractedPath.absolutePath)
        val recognizer = Recognizer(model, 16000.0f)
        speechService = SpeechService(recognizer, 16000.0f)
        lifecycleScope.launch {
            delay(10000)
            stopListeningVosk()
        }

        // Start recognition
        speechService.startListening(object : RecognitionListener {
            override fun onPartialResult(hypothesis: String?) {
            /* update UI */
                Log.d("VOSK", hypothesis?:"")
                voiceViewModel.textTranslated.value=TranscriptResult.getPartialResult(hypothesis?:"{\n" +"\"partial\" : \"\"" +"}")
            }

            override fun onResult(hypothesis: String?) { /* final result */
                Log.d("VOSK", hypothesis?:"")
                voiceViewModel.finalTextResult+=" "+TranscriptResult.getFinalResult(hypothesis?:"{\n" +"\"text\" : \"\"" +"}")
                Log.d("VOSK final result", voiceViewModel.finalTextResult)
                voiceViewModel.textTranslated.value=voiceViewModel.finalTextResult
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
            voiceViewModel.finalTextResult+=" "+voiceViewModel.textTranslated.value
            Log.d("VOSK final result", voiceViewModel.finalTextResult)
            voiceViewModel.textTranslated.value=voiceViewModel.finalTextResult
        }

    }
}

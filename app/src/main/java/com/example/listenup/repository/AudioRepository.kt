package com.example.listenup.repository

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.listenup.ListenUpApp
import com.example.listenup.common.TimeConverter
import com.example.listenup.entities.VoiceRequest
import com.example.listenup.helper.AudioPlaybackSpeed
import com.example.listenup.helper.AudioPlayer
import com.example.listenup.helper.ElevenLabs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import kotlin.math.roundToLong

class AudioRepository {
    val _audioData = MutableStateFlow<ByteArray?>(null)
    var audioPlayer: AudioPlayer = AudioPlayer()
    val _textTranslated = MutableLiveData("")
    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var elevenLabs: ElevenLabs
    val _recordDuration=MutableLiveData("00:00:00")
    val recordDuration:LiveData<String>
        get() = _recordDuration
    private var startTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private var _isRunning = MutableLiveData(false)
    var finalTextResult=""
    val isRunning:LiveData<Boolean>
        get() = _isRunning
    val _isAudioPlaying = MutableLiveData(false)
    val isAudioPlaying:LiveData<Boolean>
        get() = _isAudioPlaying
    val audioPlaybackSpeedLd =MutableLiveData<AudioPlaybackSpeed>(AudioPlaybackSpeed.Medium)


    init {
        setupSpeechRecognizer()
        elevenLabs= ElevenLabs()
    }

    //<---Converted audio total duration millis and formatted time live data--->
    val _convertedAudioDurationFormattedTime = MutableLiveData("00:00:00")
    val convertedAudioDurationFormattedTime:LiveData<String>
        get() = _convertedAudioDurationFormattedTime
    val _convertedAudioDurationMillis = MutableLiveData(0L)
    val convertedAudioDurationMillis: LiveData<Long>
        get()= _convertedAudioDurationMillis
    //<------------------------------------------------------------------------->
    //<---Live data for Media runner current state long and formatted time--->
    val _convertedAudioCurrentStateMillis = MutableLiveData(0L)
    val convertedAudioCurrentPlayStateMillis: LiveData<Long>
        get() = _convertedAudioCurrentStateMillis
    val _convertedAudioCurrentStateFormattedTime = MutableLiveData("00:00:00")
    val convertedAudioCurrentStateFormattedTime:LiveData<String>
        get() = _convertedAudioCurrentStateFormattedTime
    fun updateConvertedAudioCurrentStateFormattedTime(millis:Long){
        _convertedAudioCurrentStateMillis.value=millis
        _convertedAudioCurrentStateFormattedTime.value=TimeConverter.getFormattedTimeFromMillis(millis)
    }
    //<----------------------------------------------------------------------->
    //<---handler to increment media player time when player runs--->
    val playProgressHandler = Handler(Looper.getMainLooper())

    val translatedAudioPlayProgressRunnable = object : Runnable {
        override fun run() {
            playProgressHandler.postDelayed(this, 1000)
            val newMillis= (convertedAudioCurrentPlayStateMillis.value)?.plus(1000)
            updateConvertedAudioCurrentStateFormattedTime(newMillis!!)
            val originalDuration = convertedAudioDurationMillis.value!!
            val factor = audioPlaybackSpeedLd.value!!.speed
            if(newMillis==(originalDuration/factor).roundToLong().plus(1000)){
                playProgressHandler.postDelayed(this, 1000)
                stopMediaPlayerProgressRunnable()
                updateConvertedAudioCurrentStateFormattedTime(0)
                _isAudioPlaying.value=false
            }

        }
    }
    fun startMediaPlayerProgressRunnable() {
        playProgressHandler.post(translatedAudioPlayProgressRunnable)
    }

    // Stop the Runnable
    fun stopMediaPlayerProgressRunnable() {
        playProgressHandler.removeCallbacks(translatedAudioPlayProgressRunnable)
    }
    //<--------------------------------------------------------------->
    fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(ListenUpApp.getAppInstance())
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d("SpeechRecognizer", "Ready for speech")
                Toast.makeText(
                    ListenUpApp.getAppInstance(),
                    "Listening started",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onBeginningOfSpeech() {
                Log.d("SpeechRecognizer", "Speech has begun")
                finalTextResult=finalTextResult+_textTranslated.value
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
                Toast.makeText(
                    ListenUpApp.getAppInstance(),
                    "Processing speech...",
                    Toast.LENGTH_SHORT
                ).show()
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
                Toast.makeText(
                    ListenUpApp.getAppInstance(),
                    "Speech error: $errorMsg",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResults(results: Bundle) {
                val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.get(0) ?: ""
                Log.d("SpeechRecognizer", "Final result: $text")
                finalTextResult=finalTextResult+_textTranslated.value
                _textTranslated.value=finalTextResult
                // 🎯 Send this text to ElevenLabs or update UI
                // sendToElevenLabs(text)
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val partial =
                    partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                partial?.firstOrNull()?.let {
                    Log.d("SpeechRecognizer", "Partial result: $it")
                    // Optional: show real-time transcription
                    _textTranslated.value = it
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                Log.d("SpeechRecognizer", "Event: $eventType")
                // Rarely used — can usually be left empty
            }


        })
    }

    fun startAudioRecording() {
        finalTextResult=""
        _textTranslated.value=""
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US") // or any valid BCP-47 language tag
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US")
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "en-US")


            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000)
        }
        speechRecognizer.startListening(intent)
    }

    fun stopAudioRecording() {
        speechRecognizer.stopListening()
    }

    suspend fun generateVoiceFromText(text: String, voiceId: String) {
        try {
            val response = elevenLabs.elevenLabsApi.synthesizeVoice(
                voiceId = voiceId,
                request = VoiceRequest(text = text)
            )

            if (response.isSuccessful) {
                _audioData.value = response.body()?.bytes()  // Raw audio bytes
                Toast.makeText(ListenUpApp.getAppInstance(),"Translated successfully!!",Toast.LENGTH_SHORT).show()
                audioPlayer.setupMediaPlayer(_audioData.value!!,audioPlaybackSpeedLd.value!!.speed){
                    _convertedAudioDurationMillis.value = it
                    _convertedAudioDurationFormattedTime.value = TimeConverter.getFormattedTimeFromMillis(
                        (_convertedAudioDurationMillis.value!! *audioPlaybackSpeedLd.value!!.speed).roundToLong()
                    )
                }
            } else {
                Log.e("API_ERROR", "Failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("NETWORK_ERROR", "API call failed", e)
        }
    }

    fun resumeTranslatedAudio() {
        startMediaPlayerProgressRunnable()
        _isAudioPlaying.value=true
        audioPlayer.resumeAudio(_audioData.value!!,audioPlaybackSpeedLd.value!!.speed)
    }
    fun replayTranslatedAudio(){
        updateConvertedAudioCurrentStateFormattedTime(0)
        stopMediaPlayerProgressRunnable()
        startMediaPlayerProgressRunnable()
        _isAudioPlaying.value=true
        audioPlayer.replayAudio(_audioData.value!!,audioPlaybackSpeedLd.value!!.speed)
    }

    fun pausePlayingTranslatedAudio() {
        stopMediaPlayerProgressRunnable()
        _isAudioPlaying.value=false
        audioPlayer.pauseAudio()
    }

    //<---Used while recording audio--------------------------->
    private val timerRunnable: Runnable = object : Runnable {
        override fun run() {
            val elapsed = System.currentTimeMillis() - startTime
            val time = TimeConverter.getFormattedTimeFromMillis(elapsed)
            _recordDuration.value = time

            if (isRunning.value!!) {
                handler.postDelayed(this, 1000)
            }
        }
    }


    fun startTimer(){
        if (!isRunning.value!!) {
            startTime = System.currentTimeMillis()
            _isRunning.value = true
            handler.post(timerRunnable)
        }
    }
    fun stopTimer(){
        _isRunning.value = false
        handler.removeCallbacks(timerRunnable)
    }
    //<------------------------------------------------------------>
}
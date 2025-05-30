package com.example.listenup.viewModels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listenup.languageModel.VoskModel
import com.example.listenup.repository.AudioRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VoiceViewModel : ViewModel() {
    private val audioRepository=AudioRepository()
    val audioData: StateFlow<ByteArray?> = audioRepository._audioData
    val _textTranslated= audioRepository._textTranslated
    val recordDuration=audioRepository.recordDuration
    val isRunning=audioRepository.isRunning
    val isAudioPlaying=audioRepository.isAudioPlaying
    val convertedAudioDuration=audioRepository._convertedAudioDurationFormattedTime
    val convertedAudioDurationMillis=audioRepository._convertedAudioDurationMillis
    val convertedAudioCurrentState=audioRepository._convertedAudioCurrentStateFormattedTime
    val _audioDurationMillis=audioRepository._convertedAudioDurationMillis
    val _audioCurrentStateMillis=audioRepository._convertedAudioCurrentStateMillis
    var finalTextResult=audioRepository.finalTextResult
    var selectedLanguageModel=MutableLiveData<VoskModel>(VoskModel.Default)
    val playBackSpeedLd=audioRepository.audioPlaybackSpeedLd
    fun startRecording(){
        audioRepository.startTimer()
        if(selectedLanguageModel.value ==VoskModel.Default){
            audioRepository.startAudioRecording()
        }
    }
    fun stopRecording(){
        audioRepository.stopTimer()
        if(selectedLanguageModel.value ==VoskModel.Default){
            audioRepository.stopAudioRecording()
        }
    }
    fun recorderClicked(){
        if(isRunning.value!!){
            stopRecording()
        }else{
            startRecording()
        }
    }
    fun processText(){
        viewModelScope.launch {
            audioRepository.generateVoiceFromText(_textTranslated.value!!,"21m00Tcm4TlvDq8ikWAM")
        }
    }
    fun audioPlayerClicked(){
        if(isAudioPlaying.value==false){
            audioRepository.resumeTranslatedAudio()
        }else{
            audioRepository.pausePlayingTranslatedAudio()
        }
    }
    fun replayTranslatedAudio(){
        audioRepository.replayTranslatedAudio()
    }
}

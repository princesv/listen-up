package com.example.listenup.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listenup.ListenUpApp
import com.example.listenup.entities.VoiceRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VoiceViewModel : ViewModel() {
    private val _audioData = MutableStateFlow<ByteArray?>(null)
    val audioData: StateFlow<ByteArray?> = _audioData
    val elevenLabApi=ListenUpApp.getAppInstance().elevenLabsApi
    val _textTranslated= MutableLiveData<String>()
    val textTranslation: LiveData<String>
        get() = _textTranslated

    suspend fun generateVoice(text: String, voiceId: String) {
        try {
            val response = elevenLabApi.synthesizeVoice(
                voiceId = voiceId,
                request = VoiceRequest(text = text)
            )

            if (response.isSuccessful) {
                _audioData.value = response.body()?.bytes()  // Raw audio bytes
            } else {
                Log.e("API_ERROR", "Failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("NETWORK_ERROR", "API call failed", e)
        }
    }
}

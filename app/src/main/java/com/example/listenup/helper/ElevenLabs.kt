package com.example.listenup.helper

import android.app.Application
import android.util.Log
import com.example.listenup.ListenUpApp
import com.example.listenup.api.ElevenLabsApi
import com.example.listenup.entities.VoiceRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ElevenLabs {
    val elevenLabsApi:ElevenLabsApi
        get() = ListenUpApp.getAppInstance().elevenLabsApi
}
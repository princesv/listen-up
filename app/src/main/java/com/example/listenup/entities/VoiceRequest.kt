package com.example.listenup.entities


data class VoiceRequest(
    val text: String,
    val model_id: String = "eleven_multilingual_v2",
    val voice_settings: VoiceSettings = VoiceSettings()
)
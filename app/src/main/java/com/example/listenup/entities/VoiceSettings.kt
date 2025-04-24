package com.example.listenup.entities

data class VoiceSettings(
    val stability: Float = 0.5f,
    val similarity_boost: Float = 0.8f
)
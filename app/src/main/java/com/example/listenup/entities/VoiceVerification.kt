package com.example.listenup.entities

data class VoiceVerification(
    val is_verified: Boolean,
    val language: Any,
    val requires_verification: Boolean,
    val verification_attempts: Any,
    val verification_attempts_count: Int,
    val verification_failures: List<Any>
)
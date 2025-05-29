package com.example.listenup.helper

sealed class AudioPlaybackSpeed(val speed: Float) {
    object Slow : AudioPlaybackSpeed(0.7f)
    object Medium : AudioPlaybackSpeed(1.0f)
    object Fast : AudioPlaybackSpeed(1.3f)

    override fun toString(): String {
        return when (this) {
            Slow -> "Slow (0.5x)"
            Medium -> "Normal (1.0x)"
            Fast -> "Fast (1.5x)"
        }
    }
}

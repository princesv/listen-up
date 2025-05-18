package com.example.listenup.common

class TimeConverter {
    companion object{
        fun getFormattedTimeFromMillis(millis:Long):String{
            val seconds = (millis / 1000) % 60
            val minutes = (millis / (1000 * 60)) % 60
            val hours = (millis / (1000 * 60 * 60))

            val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            return time
        }
        fun getMillisFromFormattedTime(time: String): Long {
            val parts = time.split(":")
            if (parts.size != 3) return 0

            val hours = parts[0].toLongOrNull() ?: 0
            val minutes = parts[1].toLongOrNull() ?: 0
            val seconds = parts[2].toLongOrNull() ?: 0

            val totalMillis = (hours * 3600 + minutes * 60 + seconds) * 1000
            return totalMillis
        }
    }
}
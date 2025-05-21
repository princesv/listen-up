package com.example.listenup.languageModel

sealed class VoskModel(
    val url: String,
    val dirName: String
) {
    object Default : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip",
        dirName = "model-en"
    )
    object English : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip",
        dirName = "model-en"
    )

    object Hindi : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-hi-0.22.zip",
        dirName = "model-hi"
    )

    object Spanish : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-es-0.42.zip",
        dirName = "model-es"
    )

    // Add more models as needed
}

package com.example.listenup.languageModel

sealed class VoskModel(
    val url: String,
    val dirName: String,
    val modelName: String
) {
    object Default : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip",
        dirName = "model-en",
        modelName = "vosk-model-small-en-us-0.15"
    )
    object English : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip",
        dirName = "model-en",
        modelName = "vosk-model-small-en-us-0.15"
    )

    object Hindi : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-hi-0.22.zip",
        dirName = "model-hi",
        modelName = "vosk-model-small-hi-0.22"
    )

    object Spanish : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-es-0.42.zip",
        dirName = "model-es",
        modelName = "vosk-model-small-es-0.42"
    )

    // Add more models as needed
}

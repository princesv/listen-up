package com.example.listenup.languageModel

sealed class VoskModel(
    val url: String,
    val dirName: String,
    val modelName: String,
    val modelFolderName: String
) {
    object Default : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip",
        dirName = "model-en",
        modelFolderName = "vosk-model-small-en-us-0.15",
        modelName = "English (Default)"
    )
    object English : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip",
        dirName = "model-en",
        modelFolderName = "vosk-model-small-en-us-0.15",
        modelName = "English"
    )

    object Hindi : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-hi-0.22.zip",
        dirName = "model-hi",
        modelFolderName = "vosk-model-small-hi-0.22",
        modelName = "Hindi"
    )

    object Spanish : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-es-0.42.zip",
        dirName = "model-es",
        modelFolderName = "vosk-model-small-es-0.42",
        modelName = "Spanish"
    )

    // Add more models as needed
}

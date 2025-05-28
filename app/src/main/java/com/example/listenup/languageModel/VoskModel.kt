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

    object Chinese : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-cn-0.22.zip",
        dirName = "model-cn",
        modelFolderName = "vosk-model-small-cn-0.22",
        modelName = "Chinese"
    )

    object Arabic : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-ar-mgb2-0.4.zip",
        dirName = "model-ar",
        modelFolderName = "vosk-model-ar-mgb2-0.4",
        modelName = "Arabic"
    )

    object French : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-fr-0.22.zip",
        dirName = "model-fr",
        modelFolderName = "vosk-model-small-fr-0.22",
        modelName = "French"
    )

    object Portuguese : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-pt-0.3.zip",
        dirName = "model-pt",
        modelFolderName = "vosk-model-small-pt-0.3",
        modelName = "Portuguese"
    )

    object Russian : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-ru-0.22.zip",
        dirName = "model-ru",
        modelFolderName = "vosk-model-small-ru-0.22",
        modelName = "Russian"
    )
    // Add more models as needed

    object Japanese : VoskModel(
        url = "https://alphacephei.com/vosk/models/vosk-model-small-ja-0.22.zip",
        dirName = "model-ja",
        modelFolderName = "vosk-model-small-ja-0.22",
        modelName = "Japanese"
    )
}

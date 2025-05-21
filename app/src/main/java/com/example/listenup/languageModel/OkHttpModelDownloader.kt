package com.example.listenup.languageModel

import android.content.Context
import okhttp3.*
import java.io.*
import java.util.zip.ZipInputStream

object OkHttpModelDownloader {

    fun downloadModel(
        context: Context,
        model: VoskModel,
        onProgress: (percent: Int) -> Unit,
        onComplete: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val client = OkHttpClient()

        val request = Request.Builder().url(model.url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body ?: throw IOException("Empty response body")

                    val progressBody = ProgressResponseBody(body, onProgress)
                    val inputStream = progressBody.byteStream()

                    val zipFile = File(context.cacheDir, "${model.dirName}.zip")
                    FileOutputStream(zipFile).use { output ->
                        inputStream.copyTo(output)
                    }

                    unzip(zipFile, File(context.filesDir, model.dirName))
                    zipFile.delete()
                    onComplete()

                } catch (e: Exception) {
                    onError(e)
                }
            }
        })
    }

    private fun unzip(zipFile: File, targetDirectory: File) {
        ZipInputStream(BufferedInputStream(FileInputStream(zipFile))).use { zis ->
            var ze = zis.nextEntry
            while (ze != null) {
                val file = File(targetDirectory, ze.name)
                if (ze.isDirectory) {
                    file.mkdirs()
                } else {
                    file.parentFile?.mkdirs()
                    FileOutputStream(file).use { fos ->
                        zis.copyTo(fos)
                    }
                }
                ze = zis.nextEntry
            }
        }
    }

    fun getModelPath(context: Context, model: VoskModel): File =
        File(context.filesDir, model.dirName)

    fun isModelDownloaded(context: Context, model: VoskModel): Boolean =
        getModelPath(context, model).exists()
}

package com.example.listenup.api

import com.example.listenup.entities.VoiceRequest
import com.example.listenup.entities.Voices
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ElevenLabsApi {
    @Headers("xi-api-key: sk_95198b1b5c5a15cc4327ce6733c8f191c601e592128206ee")
    @POST("v1/text-to-speech/{voice_id}")
    suspend fun synthesizeVoice(
        @Path("voice_id") voiceId: String,
        @Body request: VoiceRequest
    ): Response<ResponseBody>

    // For voice cloning (optional)
   /* @Multipart
    @Headers("xi-api-key: YOUR_API_KEY")
    @POST("v1/voices/add")
    fun addVoice(
        @Part("name") name: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Call<VoiceResponse>

    */

    @GET("v1/voices")
    suspend fun getVoices(): Call<Voices>

}





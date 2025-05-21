package com.example.listenup.languageModel

import org.json.JSONObject

class TranscriptResult {
    companion object{
        fun getPartialResult(hypo:String):String{
            val jsonObject = JSONObject(hypo)
            val text = jsonObject.getString("partial")
            return text
        }
        fun getFinalResult(hypo:String):String{
            val jsonObject = JSONObject(hypo)
            val text = jsonObject.getString("text")
            return text
        }
    }
}
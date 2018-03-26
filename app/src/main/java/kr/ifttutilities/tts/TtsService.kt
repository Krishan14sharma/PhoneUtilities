package kr.ifttutilities.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.LANG_MISSING_DATA
import java.util.*

/**
 * Created by krishan on 16/03/18.
 */
class TtsService(val context: Context, private val listener: OnInitListener) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech = TextToSpeech(context, this).also {
        it.setSpeechRate(0.9f)
    }
    private val defaultLanguage = Locale.ENGLISH

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(defaultLanguage)
            if (result == LANG_MISSING_DATA) {
                listener.onTtsInitFailed("Missing language data")
                return
            }
            listener.onTtsInitSuccess()
        } else
            listener.onTtsInitFailed("Initialization failed")
    }

    interface OnInitListener {
        fun onTtsInitSuccess()
        fun onTtsInitFailed(error: String)
    }

    fun speak(message: String?) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null)
    }

    fun shutDown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}
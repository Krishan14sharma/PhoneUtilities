package kr.ifttutilities.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.ifttutilities.AppPreferenceManager
import kr.ifttutilities.tts.TtsService
import kr.ifttutilities.uber.getUberLastEventMessage
import kr.ifttutilities.uber.setUberLastEventMessage


class IftttFcmService : FirebaseMessagingService(), TtsService.OnInitListener {
    lateinit var tts: TtsService
    private var remoteMessage: RemoteMessage? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        this.remoteMessage = remoteMessage
        tts = TtsService(this, this)
    }

    override fun onTtsInitSuccess() {
        val message = remoteMessage?.data?.get("message")
        val id = remoteMessage?.data?.get("id")
        val speak = remoteMessage?.data?.get("speak")
        if (AppPreferenceManager.getInstance(this).getUberLastEventMessage() != message) {
            if (speak.equals("true") && !message.isNullOrEmpty())
                tts.speak(message)
        } else {
            // duplicate event fired
        }
        message?.let {
            AppPreferenceManager.getInstance(this).setUberLastEventMessage(message)
        }
    }

    override fun onTtsInitFailed(error: String) {

    }


    override fun onDestroy() {
        super.onDestroy()
        tts.shutDown()
    }

}
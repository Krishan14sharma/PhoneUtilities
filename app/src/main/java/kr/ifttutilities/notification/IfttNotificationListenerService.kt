package kr.ifttutilities.notification

import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.annotation.RequiresApi
import kr.ifttutilities.tts.TtsService


/**
 * Created by krishan on 16/03/18.
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
class IfttNotificationListenerService : NotificationListenerService(), TtsService.OnInitListener {

    lateinit var tts: TtsService
    private var isTtsInitialised = false
    private var messageToSpeak = ""
    private val TAG = "NotificationManager"

    override fun onListenerConnected() {
        super.onListenerConnected()
        tts = TtsService(this, this)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val text = sbn.notification.extras?.getString("android.text")
        if (sbn.packageName == "com.ubercab") {
            // todo send uber arriving message to band
        }

//        messageToSpeak = if (isTtsInitialised) {
//            tts.speak(text)
//            ""
//        } else {
//            text.toString()
//    }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        tts.shutDown()
    }

    override fun onTtsInitSuccess() {
        isTtsInitialised = true
        tts.speak(messageToSpeak)
    }

    override fun onTtsInitFailed(error: String) {
        isTtsInitialised = false
    }
}
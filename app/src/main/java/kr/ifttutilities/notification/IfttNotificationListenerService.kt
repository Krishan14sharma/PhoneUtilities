package kr.ifttutilities.notification

import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.annotation.RequiresApi
import kr.ifttutilities.bleMiClient.ACTION_SEND_DATA
import kr.ifttutilities.bleMiClient.EXTRA_MESSAGE
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
            handleUber(text)
        }

//        messageToSpeak = if (isTtsInitialised) {
//            tts.speak(text)
//            ""
//        } else {
//            text.toString()
//    }
    }

    private fun handleUber(text: String?) {
        if (text == null) return
        if (text.contains("Arriving soon")) {
            sendBroadcast(Intent(ACTION_SEND_DATA).also { it.putExtra(EXTRA_MESSAGE, "Cab arriving") })
        }
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
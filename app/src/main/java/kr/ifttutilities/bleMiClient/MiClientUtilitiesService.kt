package kr.ifttutilities.bleMiClient

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import kr.ifttutilities.R
import kr.ifttutilities.sms.SmsReceiver
import kr.ifttutilities.toast


class MiClientUtilitiesService : Service(), SmsReceiver.OtpCaptureListener {
    val TAG = "MiUtilitiesService"
    lateinit var helper: MiBandHelper
    lateinit var smsReceiver: SmsReceiver

    override fun onCreate() {
        super.onCreate()
        smsReceiver = SmsReceiver()
        smsReceiver.otpCaptureListener = this
        smsReceiver.register(this)

        val pendingIntent = PendingIntent.getActivity(this, 0,
                MiClientActivity.createIntent(this), 0)
        val notification = NotificationCompat.Builder(this)
                .setContentTitle("Ifttt Utility Service")
                .setContentText("Connected")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_work_black)
                .build()
        startForeground(1024, notification)
        helper = MiBandHelper(this)
        Log.d(TAG, "started")
        helper.connectBand()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        toast("running")
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return MiBinder()
    }

    override fun capturedOtp(otp: String) {
        sendMessageToMiBand(otp)
    }

    fun sendMessageToMiBand(message: String) {
        helper.sendMessageNotification(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        toast("destroyed")
        helper.disconnect()
        smsReceiver.unregister(this)
    }

    inner class MiBinder : Binder() {
        fun getService(): MiClientUtilitiesService {
            return this@MiClientUtilitiesService
        }
    }

    fun disconnect() {
        Log.d(TAG, "stopping service")
        stopForeground(true)
        stopSelf()
    }

}

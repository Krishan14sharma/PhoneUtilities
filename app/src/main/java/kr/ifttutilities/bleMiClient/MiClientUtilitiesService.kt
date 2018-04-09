package kr.ifttutilities.bleMiClient

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import kr.ifttutilities.R
import kr.ifttutilities.sms.SmsReceiver
import kr.ifttutilities.toast


const val EXTRA_MESSAGE = "kr.iffttutilities.EXTRA_MESSAGE"
const val ACTION_SEND_DATA = "kr.ifttutilities.action_send_mi_message"

class MiClientUtilitiesService : Service(), SmsReceiver.OtpCaptureListener {
    val TAG = "MiUtilitiesService"
    lateinit var helper: MiBandHelper
    lateinit var smsReceiver: SmsReceiver
    lateinit var actionReceiver: ActionReceiver
    val NOTIFICATION_CHANNEL_ID = "0"

    override fun onCreate() {
        super.onCreate()
        smsReceiver = SmsReceiver()
        smsReceiver.otpCaptureListener = this
        smsReceiver.register(this)

        actionReceiver = ActionReceiver()
        registerReceiver(actionReceiver, IntentFilter(ACTION_SEND_DATA))

        val pendingIntent = PendingIntent.getActivity(this, 0,
                MiClientActivity.createIntent(this), 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel("0", "ifttMi", NotificationManager.IMPORTANCE_DEFAULT)
            notifManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
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
        unregisterReceiver(actionReceiver)
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

    inner class ActionReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ACTION_SEND_DATA) {
                sendMessageToMiBand(intent.getStringExtra(EXTRA_MESSAGE))
            }
        }

    }

}

package kr.ifttutilities.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsMessage
import kr.ifttutilities.catchAll


/**
 * Created by krishan on 25/03/18.
 */
class SmsReceiver : BroadcastReceiver() {

    var otpCaptureListener: OtpCaptureListener? = null

    override fun onReceive(p0: Context?, intent: Intent) {
        analyzeMessages(intent)
    }

    private fun analyzeMessages(intent: Intent) {
        val bundle = intent.extras
        catchAll {
            bundle?.let {
                val pdusObj = bundle.get("pdus") as Array<*>
                for (i in pdusObj.indices) {
                    val currentMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    val phoneNumber = currentMessage.displayOriginatingAddress
                    val message = currentMessage.displayMessageBody
                    val (isOtp, otp) = OtpAnalyzer.analyze(message)
                    if (isOtp) {
                        otpCaptureListener?.capturedOtp(otp)
                    }
                }
            }
        }
    }


    fun register(context: Context) {
        val ifilter = IntentFilter()
        ifilter.priority = 1000
        ifilter.addAction("android.provider.Telephony.SMS_RECEIVED")
        context.registerReceiver(this, ifilter)
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(this)
    }

    interface OtpCaptureListener {
        fun capturedOtp(otp: String)
    }

}

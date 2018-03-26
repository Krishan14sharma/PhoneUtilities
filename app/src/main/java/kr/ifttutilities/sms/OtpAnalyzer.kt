package kr.ifttutilities.sms

import java.util.regex.Pattern

/**
 * Created by krishan on 26/03/18.
 */
object OtpAnalyzer {

    private val otpPattern = Pattern.compile("([0-9]{6})")
    /**
     * 1. xxxxxx is your Google verification code
     * 2. G-xxxxxx is your Google verification code
     * 3.Your Google verification code is xxxxxx
     */
    private val googlePattern = Pattern.compile("Google verification code")

    @JvmStatic
    fun analyze(message: String): Pair<Boolean, String> {
        return if (isGoogleVerificationCode(message) || isOtherOtp(message)) {
            val matcher = otpPattern.matcher(message)
            return if (matcher.find()) {
                Pair(true, matcher.group(1))
            } else Pair(false, "")
        } else
            Pair(false, "")

    }

    private fun isOtherOtp(message: String) = message.contains("OTP", true)


    fun isGoogleVerificationCode(message: String) = googlePattern.matcher(message).find()

}
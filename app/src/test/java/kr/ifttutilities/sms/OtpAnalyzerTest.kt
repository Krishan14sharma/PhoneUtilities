package kr.ifttutilities.sms

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by krishan on 26/03/18.
 */
class OtpAnalyzerTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testGoogleRegexForCaseOne() {
        val correctMessage = "Your Google verification code is 123456"
        assertThat(OtpAnalyzer.isGoogleVerificationCode(correctMessage)).isEqualTo(true)
        val analyze = OtpAnalyzer.analyze(correctMessage)
        assertThat(analyze.first).isEqualTo(true)
        assertThat(analyze.second).isEqualTo("123456")
    }

    @Test
    fun testGoogleRegexForCaseTwo() {
        val correctMessage = "G-123456 is your Google verification code"
        assertThat(OtpAnalyzer.isGoogleVerificationCode(correctMessage)).isEqualTo(true)
        val analyze = OtpAnalyzer.analyze(correctMessage)
        assertThat(analyze.first).isEqualTo(true)
        assertThat(analyze.second).isEqualTo("123456")
    }

    @Test
    fun testGoogleRegexForCaseThree() {
        val correctMessage = "Your Google verification code is 123456"
        assertThat(OtpAnalyzer.isGoogleVerificationCode(correctMessage)).isEqualTo(true)
        val analyze = OtpAnalyzer.analyze(correctMessage)
        assertThat(analyze.first).isEqualTo(true)
        assertThat(analyze.second).isEqualTo("123456")
    }

    @Test
    fun testOtherOtp() {
        val correctMessage = "You have initiated a txn of INR 1799.00 at Amazon Ind on ICICI Bank Card no.0936. OTP is 123457."
        val analyze = OtpAnalyzer.analyze(correctMessage)
        assertThat(analyze.first).isEqualTo(true)
        assertThat(analyze.second).isEqualTo("123457")
    }

}
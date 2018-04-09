package kr.ifttutilities.bleMiClient

import android.annotation.SuppressLint
import kr.ifttutilities.AppPreferenceManager

/**
 * Created by krishan on 27/03/18.
 */

const val PREF_MI_BAND_ADDRESS = "mi_band_address"

@SuppressLint("ApplySharedPref")
fun AppPreferenceManager.setMiBandAddress(address: String) {
    sharedPreferences.edit().putString(PREF_MI_BAND_ADDRESS, address).commit()
}

fun AppPreferenceManager.getMiBandAddress(): String {
    return sharedPreferences.getString(PREF_MI_BAND_ADDRESS, "")
}
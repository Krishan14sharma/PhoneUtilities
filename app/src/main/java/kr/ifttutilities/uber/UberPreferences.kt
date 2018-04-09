package kr.ifttutilities.uber

import android.annotation.SuppressLint
import kr.ifttutilities.AppPreferenceManager

/**
 * Created by krishan on 08/03/18.
 */

const val PREF_KEY_POOL_PRODUCT_ID = "uber_pool_product_id"
const val PREF_KEY_POOL_LAST_EVENT_ID = "uber_pool_last_event_id"

fun AppPreferenceManager.setUberPoolProductId(productId: String) {
    sharedPreferences.edit().putString(PREF_KEY_POOL_PRODUCT_ID, productId).apply()
}

fun AppPreferenceManager.getUberPoolProductId(): String {
    return sharedPreferences.getString(PREF_KEY_POOL_PRODUCT_ID, "")
}

@SuppressLint("ApplySharedPref")
fun AppPreferenceManager.setUberLastEventMessage(eventId: String) {
    sharedPreferences.edit().putString(PREF_KEY_POOL_LAST_EVENT_ID, eventId).commit()
}

fun AppPreferenceManager.getUberLastEventMessage(): String {
    return sharedPreferences.getString(PREF_KEY_POOL_LAST_EVENT_ID, "")
}
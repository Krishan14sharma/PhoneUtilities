package kr.ifttutilities.uber

import kr.ifttutilities.AppPreferenceManager

/**
 * Created by krishan on 08/03/18.
 */

const val PREF_KEY_POOL_PRODUCT_ID = "poolId"
const val PREF_KEY_POOL_LAST_EVENT_ID = "poolId"

fun AppPreferenceManager.setUberPoolProductId(productId: String) {
    sharedPreferences.edit().putString(PREF_KEY_POOL_PRODUCT_ID, productId).apply()
}

fun AppPreferenceManager.getUberPoolProductId(): String {
    return sharedPreferences.getString(PREF_KEY_POOL_PRODUCT_ID, "")
}

fun AppPreferenceManager.setUberLastEventMessage(eventId: String) {
    sharedPreferences.edit().putString(PREF_KEY_POOL_LAST_EVENT_ID, eventId).commit()
}

fun AppPreferenceManager.getUberLastEventMessage(): String {
    return sharedPreferences.getString(PREF_KEY_POOL_LAST_EVENT_ID, "")
}
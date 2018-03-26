package kr.ifttutilities

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by krishan on 08/03/18.
 */
class AppPreferenceManager private constructor(val sharedPreferences: SharedPreferences) {

    val PREF_READ_NOTIFICATION_LOUD = "pref_read_notification_loud"

    companion object {
        const private val preferenceName = "kr.ifttutilities"
        @Volatile
        private var INSTANCE: AppPreferenceManager? = null

        fun getInstance(context: Context): AppPreferenceManager {
            return INSTANCE ?: synchronized(this) {
                return INSTANCE ?: AppPreferenceManager(context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE))
                        .also { INSTANCE = it }
            }
        }
    }

    fun setReadNotificationAloudPreference(state: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_READ_NOTIFICATION_LOUD, state).apply()
    }

    fun getReadNotificationAloudPreference(): Boolean {
        return sharedPreferences.getBoolean(PREF_READ_NOTIFICATION_LOUD, false)
    }


}
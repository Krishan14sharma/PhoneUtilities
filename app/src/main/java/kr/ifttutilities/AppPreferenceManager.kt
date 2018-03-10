package kr.ifttutilities

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by krishan on 08/03/18.
 */
class AppPreferenceManager private constructor(val sharedPreferences: SharedPreferences) {
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

}
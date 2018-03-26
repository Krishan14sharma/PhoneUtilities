package kr.ifttutilities

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Created by krishan on 22/03/18.
 */
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.applicationContext, message, duration).show()
}

fun Any.catchAll(action: () -> Unit) {
    try {
        action()
    } catch (exception: Exception) {
        Log.d("Error", exception.message)
    }
}
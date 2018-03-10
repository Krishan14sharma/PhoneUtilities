package kr.ifttutilities

import android.app.Application
import com.uber.sdk.android.core.UberSdk
import kr.ifttutilities.uber.UberUtils


/**
 * Created by krishan on 08/03/18.
 */
class IfttApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        UberSdk.initialize(UberUtils.getUberSessionConfiguration())
    }
}
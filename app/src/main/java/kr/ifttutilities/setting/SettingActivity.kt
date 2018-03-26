package kr.ifttutilities.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_setting.*
import kr.ifttutilities.AppPreferenceManager
import kr.ifttutilities.R

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val preferenceManager = AppPreferenceManager.getInstance(this)
        readNotificationSwitch.isChecked = preferenceManager.getReadNotificationAloudPreference()
        readNotificationSwitch.setOnCheckedChangeListener { button, state ->
            preferenceManager.setReadNotificationAloudPreference(state)
            if (state) {
                Toast.makeText(this, "give us notification permission", Toast.LENGTH_SHORT).show()
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                startActivity(intent)
            }
        }

    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SettingActivity::class.java)
        }
    }
}

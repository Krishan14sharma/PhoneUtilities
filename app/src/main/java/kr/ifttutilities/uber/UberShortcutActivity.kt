package kr.ifttutilities.uber

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kr.ifttutilities.R
import kr.ifttutilities.uber.main.UberMainFragment

class UberShortcutActivity : AppCompatActivity() {

    companion object {
        //todo convert to enum
        const val EXTRA_KEY_ACTION = "keyAction"
        const val ACTION_HOME_TO_WORK = "action_home_to_work"
        const val ACTION_WORK_TO_HOME = "action_work_to_home"
        const val ACTION_TO_HOME = "action_to_home"

        fun createIntent(context: Context, action: String): Intent {
            return Intent(context, UberShortcutActivity::class.java).also {
                it.putExtra(EXTRA_KEY_ACTION, action)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uber_shortcut_activiy)
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, UberMainFragment.newInstance(intent.getStringExtra(EXTRA_KEY_ACTION)))
                .commit()
    }
}

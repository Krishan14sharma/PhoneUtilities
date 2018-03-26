package kr.ifttutilities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kr.ifttutilities.bleMiClient.MiClientActivity
import kr.ifttutilities.bleServer.BleServerActivity
import kr.ifttutilities.setting.SettingActivity
import kr.ifttutilities.terminal.TerminalActivity
import kr.ifttutilities.uber.UberHomeActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uberButton.setOnClickListener {
            startActivity(UberHomeActivity.createIntent(this))
        }
        settingButton.setOnClickListener {
            startActivity(SettingActivity.createIntent(this))
        }

        terminalButton.setOnClickListener {
            startActivity(TerminalActivity.createIntent(this))
        }
        bleServerButton.setOnClickListener {
            startActivity(BleServerActivity.createIntent(this))
        }
        bleMiClientButton.setOnClickListener {
            startActivity(MiClientActivity.createIntent(this))
        }


        //todo water monitor
        //todo mi band hack
    }
}

package kr.ifttutilities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kr.ifttutilities.uber.UberHomeActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uberButton.setOnClickListener {
            startActivity(UberHomeActivity.createIntent(this))
        }
    }
}

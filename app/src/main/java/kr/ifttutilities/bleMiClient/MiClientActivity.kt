package kr.ifttutilities.bleMiClient

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mi_client.*
import kr.ifttutilities.AppPreferenceManager
import kr.ifttutilities.R
import kr.ifttutilities.toast


class MiClientActivity : AppCompatActivity(), ServiceConnection {
    private lateinit var adapter: BluetoothAdapter
    private val REQUEST_ENABLE_BT = 100
    private val REQUEST_ENABLE_READ_SMS = 102
    private val TAG = "MiClientActivity"
    private var miService: MiClientUtilitiesService? = null
    private var isMiServiceConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_client)

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            toast(getString(R.string.feature_not_supported))
            finish()
        }

        connectToServiceIfRunning()

        adapter = (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        getAllPermissions()
        connectButton.setOnClickListener {
            if (adapter.isEnabled && checkOtherPermissionsGranted()) {
                if (checkForBondedMiBandDevice()) {
                    startUtilityService()
                } else {
                    Snackbar.make(connectButton, getString(R.string.mi_band_connection_condition), Snackbar.LENGTH_LONG).show()
                }
            } else getAllPermissions()
        }

        buttonTest.setOnClickListener {
            miService?.sendMessageToMiBand("testing")
        }
        buttonDisconnect.setOnClickListener {
            miService?.disconnect()
        }
    }

    private fun connectToServiceIfRunning() {
        val serviceIntent = Intent(this, MiClientUtilitiesService::class.java)
        bindService(serviceIntent, this, 0)
    }

    private fun enableBluetooth() {
        if (!adapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    private fun checkForBondedMiBandDevice(): Boolean {
        val device = adapter.bondedDevices?.find {
            it.name.startsWith(BAND_NAME)
        }?.also {
            AppPreferenceManager.getInstance(this).setMiBandAddress(it.address)
        }
        return device != null
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        isMiServiceConnected = false
    }

    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
        isMiServiceConnected = true
        val binder = service as MiClientUtilitiesService.MiBinder
        miService = binder.getService()
    }

    private fun startUtilityService() {
        val serviceIntent = Intent(this, MiClientUtilitiesService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
    }

    private fun getAllPermissions() {
        if (!adapter.isEnabled) {
            enableBluetooth()
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS), REQUEST_ENABLE_READ_SMS)
        }
    }

    private fun checkOtherPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_ENABLE_READ_SMS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            getAllPermissions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isMiServiceConnected)
            unbindService(this)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MiClientActivity::class.java)
        }
    }

}
